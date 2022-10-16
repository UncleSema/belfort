package ru.ct.belfort.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import ru.ct.belfort.PositionDataDTO;

@Slf4j
@Service
public class OperationsProducer {
    private final KafkaTemplate<String, PositionDataDTO> operationsProducer;
    @Autowired
    public OperationsProducer(@Qualifier("OperationsProducerTemplate") KafkaTemplate<String, PositionDataDTO> operationsProducer) {
        this.operationsProducer = operationsProducer;
    }

    public void sendMessage(PositionDataDTO message) {
        ListenableFuture<SendResult<String, PositionDataDTO>> future =
                operationsProducer.send("ct.belfort.invest.operations", message);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, PositionDataDTO> result) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
    }
}