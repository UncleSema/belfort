package ru.ct.belfort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ct.belfort.client.TinkoffClientService;
import ru.ct.belfort.producer.OperationsProducer;
import ru.ct.belfort.subscribers.OperationSubscriber;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Service
public class OperationSubscriptionService {
    @NonNull
    private final OperationsProducer operationProducer;

    private final TinkoffClientService tinkoffClientService;

    public void subscribe(String token) {
        Consumer<Throwable> onErrorCallback = error -> log.error("Error while trying to subscribe on operations", error);
        tinkoffClientService.subscribeOperations(token, new OperationSubscriber(operationProducer), onErrorCallback);
    }

    public void unsubscribe(String token) {
        tinkoffClientService.unsubscribeOperations(token);
    }
}