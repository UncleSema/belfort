package ru.ct.belfort.client;
import ru.ct.belfort.exceptions.NoTinkoffAccountException;
import ru.ct.belfort.subscribers.CandleSubscriber;
import ru.ct.belfort.subscribers.OperationSubscriber;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.contract.v1.AccountType;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;

import java.util.List;
import java.util.function.Consumer;

public class TinkoffClient {
    private final InvestApi api;

    public TinkoffClient(String token) {
        api = InvestApi.create(token);
    }

    public List<Account> getAccountsList() {
        return api.getUserService().getAccountsSync();
    }

    public boolean isTokenValid() {
        boolean answer = true;
        try {
            getAccountsList();
        } catch (ApiRuntimeException e) {
            answer = !e.getCode().equals("40003");
        }
        return answer;
    }

    public boolean isFigiValid(String figi) {
        boolean answer = true;
        try {
            api.getInstrumentsService().getCurrencyByFigi(figi);
        } catch (ApiRuntimeException e) {
            answer = !e.getCode().equals("50002");
        }
        return answer;
    }

    public boolean isListOfFigisValid(List<String> figis) {
        boolean yes = true;
        for (String figi : figis) {
            yes &= isFigiValid(figi);
        }
        return yes;
    }

    public void subscribeCandles(List<String> figis, CandleSubscriber subscriber, Consumer<Throwable> onErrorCallback) {
        var subsService =
                api.getMarketDataStreamService().newStream("candles_stream", subscriber, onErrorCallback);
        subsService.subscribeCandles(figis);
    }

    public void unsubscribeCandles(List<String> figis) {
        var subsService = api.
                getMarketDataStreamService().
                getStreamById("candles_stream");
        subsService.unsubscribeCandles(figis);
    }

    public void subscribeOperations(OperationSubscriber subscriber, Consumer<Throwable> onErrorCallback) {
        List<Account> accounts = getAccountsList();
        List<Account> br = accounts.stream().filter(s -> s.getType() == AccountType.ACCOUNT_TYPE_TINKOFF).toList();
        if (br.isEmpty()) {
            throw new NoTinkoffAccountException();
        }
        var mainAccountId = br.get(0).getId();
        api.getOperationsStreamService().subscribePositions(
                subscriber,
                onErrorCallback,
                mainAccountId);
    }

    public void unsubscribeOperations() {
        //TODO()
    }
}
