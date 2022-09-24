package ru.ct.belfort.kafka.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.strategy.StratController;

@Service
public class CandlesConsumer {

    private final StratController stratController;

    public CandlesConsumer(StratController stratController) {
        this.stratController = stratController;
    }

    @KafkaListener(topics = "ct.belfort.invest.candles", groupId = "trading_bot_consumers")
    public void listen(TradingInfoDTO message) {
        System.out.println("Received message: " + message);

        stratController.dispense(message);
    }

}
