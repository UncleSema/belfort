package ru.ct.belfort.kafka.consumers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.strategy.StrategyController;

@Service
@Slf4j
public class CandlesConsumer {

    private final StrategyController stratController;

    public CandlesConsumer(StrategyController stratController) {
        this.stratController = stratController;
    }

    @KafkaListener(topics = "ct.belfort.invest.candles", groupId = "candle_consumers")
    public void listen(TradingInfoDTO message) {
        log.info("CandlesConsumer got message: " + message);
        stratController.dispense(message);
    }

}
