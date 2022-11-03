package ru.ct.belfort.idea;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.ct.belfort.IdeaDTO;
import ru.ct.belfort.Advice;
import ru.ct.belfort.kafka.producers.ErrorProducer;
import ru.ct.belfort.kafka.producers.IdeasProducer;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SimpleIdeaGenerator implements IdeaGenerator {

    IdeasProducer ideasProducer;
    ErrorProducer errorProducer;

    @Override
    public void generateIdea(double score) {
        if (score < 30) {
            ideasProducer.sendMessage(new IdeaDTO(score, Advice.BUY));
        } else if (score > 70) {
            ideasProducer.sendMessage(new IdeaDTO(score, Advice.SELL));
        }
    }

    @Override
    public void generateError(String message) {
        errorProducer.sendMessage(message);
    }
}
