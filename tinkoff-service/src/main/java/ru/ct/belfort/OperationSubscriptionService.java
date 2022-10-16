package ru.ct.belfort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ct.belfort.exceptions.NoTinkoffAccountException;
import ru.ct.belfort.producer.OperationsProducer;
import ru.ct.belfort.subscribers.OperationSubscriber;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.contract.v1.AccountType;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Service
public class OperationSubscriptionService {
    @NonNull
    private final OperationsProducer operationProducer;

    private final Map<String, InvestApi> api = new HashMap<>();

    private InvestApi getApiByToken(String token) {
        return api.computeIfAbsent(token, InvestApi::create);
    }

    public void subscribe(String token) {
        var currentApi = getApiByToken(token);
        List<Account> accounts = currentApi.getUserService().getAccountsSync();
        List<Account> br = accounts.stream().filter(s -> s.getType() == AccountType.ACCOUNT_TYPE_TINKOFF).toList();
        if (br.isEmpty()) {
            throw new NoTinkoffAccountException();
        }
        var mainAccountId = br.get(0).getId();
        Consumer<Throwable> onErrorCallback = error -> log.error(error.toString());
        currentApi.getOperationsStreamService().subscribePositions(
                new OperationSubscriber(operationProducer),
                onErrorCallback,
                mainAccountId);
    }

    public void unsubscribe(String token) {
        //TODO: check is that right?
        api.remove(token);
    }
}