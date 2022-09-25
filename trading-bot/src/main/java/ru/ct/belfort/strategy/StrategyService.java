package ru.ct.belfort.strategy;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.ct.belfort.IdeaDTO;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.kafka.producers.IdeasProducer;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrategyService {

    IdeasProducer ideasProducer;
    AllStrategies allStrategies;

    public void dispense(TradingInfoDTO dto) {

        double result = switch (dto.strategy()) {
            case "test" -> allStrategies.testStrategy.predict(dto.candles());
            case "rsi" -> allStrategies.rsiStrategy.predict(dto.candles());
            default -> throw new RuntimeException("Unknown strategy!"); // TODO: provide error to tinkoff-service?
        };

        IdeaDTO idea = new IdeaDTO(result, "Some meta info");
        ideasProducer.sendMessage(idea);
    }
}
