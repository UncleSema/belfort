package ru.ct.belfort.idea;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.ct.belfort.IdeaDTO;
import ru.ct.belfort.Advice;
import ru.ct.belfort.db.IdeasRepository;
import ru.ct.belfort.kafka.producers.ErrorProducer;
import ru.ct.belfort.kafka.producers.IdeasProducer;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SimpleIdeaGenerator implements IdeaGenerator {

    IdeasProducer ideasProducer;
    ErrorProducer errorProducer;
    IdeasRepository ideasRepository;

    @Override
    public void generateIdea(double score) {
        if (score > 30 && score < 70) {
            return;
        }
        var idea = new IdeaDTO(score, score < 30 ? Advice.BUY : Advice.SELL);
        ideasProducer.sendMessage(idea);
        ideasRepository.insert(idea);
    }

    @Override
    public void generateError(String message) {
        errorProducer.sendMessage(message);
    }
}
