package ru.ct.belfort.strategy;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.idea.SimpleIdeaGenerator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrategyService {

    Map<String, StrategyInterface> strats;
    SimpleIdeaGenerator generator;

    @Autowired
    public StrategyService(List<StrategyInterface> stratsList, SimpleIdeaGenerator generator) {
        strats = stratsList
                .stream()
                .collect(Collectors.toMap(
                        StrategyInterface::getQualifier,
                        Function.identity()));
        this.generator = generator;
    }

    public void dispense(TradingInfoDTO dto) {
        double result = -1;
        if (strats.containsKey(dto.strategy())) {
            result = strats.get(dto.strategy()).predict(dto.candles());
        }
        generator.generateIdea(result);
    }
}
