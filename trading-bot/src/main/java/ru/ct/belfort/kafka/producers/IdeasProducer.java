package ru.ct.belfort.kafka.producers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ct.belfort.IdeaDTO;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdeasProducer {

    KafkaTemplate<String, IdeaDTO> kafkaTemplate;

    public void sendMessage(IdeaDTO message) {
        kafkaTemplate.send(IdeasProducerConfig.TOPIC, message);
    }
}
