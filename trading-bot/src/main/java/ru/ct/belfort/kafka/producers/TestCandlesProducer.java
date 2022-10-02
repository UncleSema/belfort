package ru.ct.belfort.kafka.producers;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.kafka.consumers.CandlesConsumerConfig;

// This class is for testing. It should take place in tinkoff-service

@Service
@RequiredArgsConstructor
public class TestCandlesProducer {

    private final KafkaTemplate<String, TradingInfoDTO> kafkaTemplate;

    public void sendMessage(TradingInfoDTO message) {
        kafkaTemplate.send(CandlesConsumerConfig.TOPIC, message);
    }
}
