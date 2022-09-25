package ru.ct.belfort.strategy;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.ct.belfort.IdeaDTO;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.kafka.producers.IdeasProducer;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrategyService {

    IdeasProducer ideasProducer;
    List<StrategyInterface> strats;

    public void dispense(TradingInfoDTO dto) {
        double result = -1;
        System.out.println(strats.size());
        for (StrategyInterface strat : strats) {
            if (dto.strategy().equals(strat.getQualifier())) {
                result = strat.predict(dto.candles());
                break;
            }
        }

        IdeaDTO idea = new IdeaDTO(result, "Some meta info");
        ideasProducer.sendMessage(idea);
    }
}
