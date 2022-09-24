package ru.ct.belfort.kafka.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ct.belfort.TradingInfoDTO;

// This class is for testing. It should take place in tinkoff-service

@Service
public class TestCandlesProducer {
    private final KafkaTemplate<String, TradingInfoDTO> kafkaTemplate;

    public TestCandlesProducer(KafkaTemplate<String, TradingInfoDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(TradingInfoDTO message) {
        kafkaTemplate.send("ct.belfort.invest.candles", message);
    }
}
