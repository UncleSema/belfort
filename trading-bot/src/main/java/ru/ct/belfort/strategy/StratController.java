package ru.ct.belfort.strategy;

import org.springframework.stereotype.Service;
import ru.ct.belfort.IdeaDTO;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.kafka.producers.IdeasProducer;

@Service
public class StratController {

    private final IdeasProducer ideasProducer;

    public StratController(IdeasProducer producer) {
        this.ideasProducer = producer;
    }

    public void dispense(TradingInfoDTO dto) {
        double result = switch (dto.strategy()) {
            case "test" -> TestStrategy.predict();
            case "rsi" -> RsiStrategy.predict(dto.candles());
            default -> throw new RuntimeException("Unknown strategy!"); // TODO: provide error to tinkoff-service?
        };
        IdeaDTO idea = new IdeaDTO(result, "Some meta info");
        ideasProducer.sendMessage(idea);
    }
}
