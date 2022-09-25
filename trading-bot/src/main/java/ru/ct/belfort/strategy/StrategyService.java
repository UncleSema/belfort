package ru.ct.belfort.strategy;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ct.belfort.IdeaDTO;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.kafka.producers.IdeasProducer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrategyService {

    IdeasProducer ideasProducer;
    Map<String, StrategyInterface> strats;

    @Autowired
    public StrategyService(IdeasProducer ideasProducer, List<StrategyInterface> stratsList) {
        this.ideasProducer = ideasProducer;
        strats = stratsList
                .stream()
                .collect(Collectors.toMap(
                        StrategyInterface::getQualifier,
                        Function.identity()));
    }

    public void dispense(TradingInfoDTO dto) {
        if (!strats.containsKey(dto.strategy())) {
            throw new RuntimeException("Unknown strategy");
            // TODO: provide error to tinkoff-service?
        }
        double result = strats.get(dto.strategy()).predict(dto.candles());

        IdeaDTO idea = new IdeaDTO(result, "Some meta info");
        ideasProducer.sendMessage(idea);
    }
}