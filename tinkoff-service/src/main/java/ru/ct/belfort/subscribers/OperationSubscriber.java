package ru.ct.belfort.subscribers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ct.belfort.producer.OperationsProducer;
import ru.ct.belfort.util.Utilities;
import ru.tinkoff.piapi.contract.v1.PositionsStreamResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

@RequiredArgsConstructor
@Slf4j
public class OperationSubscriber implements StreamProcessor<PositionsStreamResponse> {

    @NonNull
    private final OperationsProducer producer;

    @Override
    public void process(PositionsStreamResponse response) {
        if (response.hasPosition()) {
            log.info("New position change!");
            log.info(response.getPosition().toString());

            producer.sendMessage(Utilities.create(response.getPosition()));
        } else {
            log.warn("Got unknown message: {}", response);
        }
    }
}