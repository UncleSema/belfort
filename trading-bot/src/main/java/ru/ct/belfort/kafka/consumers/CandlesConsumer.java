package ru.ct.belfort.kafka.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CandlesConsumer {

    @KafkaListener(topics = "ct.belfort.invest.candles", groupId = "trading_bot_consumers")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }

}
