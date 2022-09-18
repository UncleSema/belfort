package ru.ct.belfort.Kafka.Consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OperationsConsumer {

    @KafkaListener(topics = "ct.belfort.invest.operations", groupId = "telegram-service-consumers")
    public void consume(String token) {
        //  Andrew's producer -> this
    }
}
