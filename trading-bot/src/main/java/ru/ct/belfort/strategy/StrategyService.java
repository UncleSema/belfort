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
    AllStrategies strats;

    public void dispense(TradingInfoDTO dto) {
        double result;
        if (dto.strategy().equals(strats.test.getQualifier())) {
            result = strats.test.predict(dto.candles());
        } else if (dto.strategy().equals(strats.rsi.getQualifier())) {
            result = strats.rsi.predict(dto.candles());
        } else {
            // TODO: provide error to tinkoff-service?
            throw new RuntimeException("Unknown strategy!");
        }

        IdeaDTO idea = new IdeaDTO(result, "Some meta info");
        ideasProducer.sendMessage(idea);
    }
}
