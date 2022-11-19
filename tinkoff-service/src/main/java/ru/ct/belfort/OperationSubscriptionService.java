package ru.ct.belfort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ct.belfort.client.TinkoffClient;
import ru.ct.belfort.producer.OperationsProducer;
import ru.ct.belfort.subscribers.OperationSubscriber;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Service
public class OperationSubscriptionService {
    @NonNull
    private final OperationsProducer operationProducer;

    public void subscribe(TinkoffClient client) {
        Consumer<Throwable> onErrorCallback = error -> log.error(error.toString());
        client.subscribeOperations(new OperationSubscriber(operationProducer), onErrorCallback);
    }

    public void unsubscribe(TinkoffClient client) {
        client.unsubscribeOperations();
    }
}