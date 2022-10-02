package ru.ct.belfort.kafka.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.kafka.KafkaConfig;
import ru.ct.belfort.strategy.StrategyService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandlesConsumer {

    private final StrategyService strategyService;

    @KafkaListener(topics = KafkaConfig.CANDLES_TOPIC,
                   groupId = CandlesConsumerConfig.GROUP_ID,
                   containerFactory = "candlesConsumerContainerFactory")
    public void listen(TradingInfoDTO message) {
        log.info("CandlesConsumer got message: " + message);
        strategyService.dispense(message);
    }
}
