package ru.ct.belfort.idea;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.ct.belfort.IdeaDTO;
import ru.ct.belfort.kafka.producers.IdeasProducer;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SimpleIdeaGenerator implements IdeaGenerator {

    IdeasProducer ideasProducer;

    @Override
    public void generateIdea(double coefficient) {
        if (coefficient == -1) {
            ideasProducer.sendError("Unknown strategy");
        } else if (coefficient < 30) {
            ideasProducer.sendMessage(new IdeaDTO(coefficient, "Recommended to buy"));
        } else if (coefficient > 70) {
            ideasProducer.sendMessage(new IdeaDTO(coefficient, "Recommended to sell"));
        }
    }
}
