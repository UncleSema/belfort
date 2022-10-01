package ru.ct.belfort.kafka.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.strategy.StrategyService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandlesConsumer {

    private final StrategyService stratController;

    @KafkaListener(topics = "ct.belfort.invest.candles",
            groupId = "candle_consumers",
            containerFactory = "candlesConsumerContainerFactory")
    public void listen(TradingInfoDTO message) {
        log.info("CandlesConsumer got message: " + message);
        stratController.dispense(message);
    }

}
