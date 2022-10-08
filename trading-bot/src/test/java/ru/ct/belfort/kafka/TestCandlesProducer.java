package ru.ct.belfort.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.kafka.consumers.CandlesConsumerConfig;

@Service
@RequiredArgsConstructor
public class TestCandlesProducer {

    private final KafkaTemplate<String, TradingInfoDTO> kafkaTemplate2;

    public void sendMessage(TradingInfoDTO message) {
        kafkaTemplate2.send(CandlesConsumerConfig.TOPIC, message);
    }
}
