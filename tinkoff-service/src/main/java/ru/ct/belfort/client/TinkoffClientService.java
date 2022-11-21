package ru.ct.belfort.client;

import org.springframework.stereotype.Service;
import ru.ct.belfort.exceptions.NoTinkoffAccountException;
import ru.ct.belfort.subscribers.CandleSubscriber;
import ru.ct.belfort.subscribers.OperationSubscriber;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.contract.v1.AccountType;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class TinkoffClientService {
    private final Map<String, InvestApi> api = new HashMap<>();
    public InvestApi getApiByToken(String token) {
        return api.computeIfAbsent(token, InvestApi::create);
    }
    public void removeApiByToken(String token) {
        //assuming that we have that token in 'api'
        api.remove(token);
    }

    public List<Account> getAccountsList(String token) {
        return getApiByToken(token).getUserService().getAccountsSync();
    }

    public boolean isTokenValid(String token) {
        boolean answer = true;
        try {
            getAccountsList(token);
        } catch (ApiRuntimeException e) {
            answer = !"40003".equals(e.getCode());
        }
        if (!answer) {
            removeApiByToken(token);
        }
        return answer;
    }

    public boolean isFigiValid(String token, String figi) {
        boolean answer = true;
        try {
            getApiByToken(token).getInstrumentsService().getCurrencyByFigi(figi);
        } catch (ApiRuntimeException e) {
            answer = !"50002".equals(e.getCode());
        }
        return answer;
    }

    public boolean isListOfFigisValid(String token, List<String> figis) {
        return figis.stream().allMatch(x -> isFigiValid(token, x));
    }

    public void subscribeCandles(String token,
                                 List<String> figis,
                                 CandleSubscriber subscriber, Consumer<Throwable> onErrorCallback) {
        var subsService =
                getApiByToken(token)
                        .getMarketDataStreamService()
                        .newStream("candles_stream", subscriber, onErrorCallback);
        subsService.subscribeCandles(figis);
    }

    public void unsubscribeCandles(String token, List<String> figis) {
        var subsService = getApiByToken(token).
                getMarketDataStreamService().
                getStreamById("candles_stream");
        subsService.unsubscribeCandles(figis);
    }

    public void subscribeOperations(String token, OperationSubscriber subscriber, Consumer<Throwable> onErrorCallback) {
        List<Account> accounts = getAccountsList(token);
        Optional<Account> br = accounts
                .stream()
                .filter(s -> s.getType() == AccountType.ACCOUNT_TYPE_TINKOFF)
                .findAny();
        if (br.isEmpty()) {
            throw new NoTinkoffAccountException();
        }
        var mainAccountId = br.get().getId();
        getApiByToken(token).getOperationsStreamService().subscribePositions(
                subscriber,
                onErrorCallback,
                mainAccountId);
    }

    public void unsubscribeOperations(String token) {
        //TODO()
    }
}
