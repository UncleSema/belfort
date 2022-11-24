package ru.ct.belfort.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ct.belfort.exceptions.NoTinkoffAccountException;
import ru.ct.belfort.subscribers.CandleSubscriber;
import ru.ct.belfort.subscribers.OperationSubscriber;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;

import java.util.*;
import java.util.function.Consumer;

@Slf4j
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

    private String getMainAccountId(String token) {
        List<Account> accounts = getAccountsList(token);
        Optional<Account> br = accounts
                .stream()
                .filter(s -> s.getType() == AccountType.ACCOUNT_TYPE_TINKOFF)
                .findAny();
        if (br.isEmpty()) {
            throw new NoTinkoffAccountException();
        }
        return br.get().getId();
    }

    private Quotation getLastPriceByFigi(String token, String figi) {
        var currentApi = getApiByToken(token);
        return currentApi
                .getMarketDataService()
                .getLastPricesSync(List.of(figi))
                .get(0)
                .getPrice();
    }

    public void subscribeOperations(String token, OperationSubscriber subscriber, Consumer<Throwable> onErrorCallback) {
        var mainAccountId = getMainAccountId(token);
        getApiByToken(token).getOperationsStreamService().subscribePositions(
                subscriber,
                onErrorCallback,
                mainAccountId);
    }

    public void unsubscribeOperations(String token) {
        //TODO()
    }

    //Should I handle exceptions? (For example if there is no lots of that figi?)
    public void sellByFigi(String token, String figi) {
        var mainAccountId = getMainAccountId(token);
        var currentApi = getApiByToken(token);
        var lastPrice = getLastPriceByFigi(token, figi);
        currentApi
                .getOrdersService()
                .postOrderSync(figi,
                        1,
                        lastPrice,
                        OrderDirection.ORDER_DIRECTION_SELL,
                        mainAccountId,
                        OrderType.ORDER_TYPE_LIMIT,
                UUID.randomUUID().toString());
        log.info("Posted order to sell figi=" + figi);
    }

    //Should I handle exceptions? (For example if there is no money?)
    public void buyByFigi(String token, String figi) {
        var mainAccountId = getMainAccountId(token);
        var currentApi = getApiByToken(token);
        var lastPrice = getLastPriceByFigi(token, figi);
        currentApi
                .getOrdersService()
                .postOrderSync(figi,
                        1,
                        lastPrice,
                        OrderDirection.ORDER_DIRECTION_BUY,
                        mainAccountId,
                        OrderType.ORDER_TYPE_LIMIT,
                        UUID.randomUUID().toString());
        log.info("Posted order to buy figi=" + figi);
    }
}
