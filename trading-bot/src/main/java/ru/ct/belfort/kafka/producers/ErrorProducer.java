package ru.ct.belfort.kafka.producers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorProducer {

    KafkaTemplate<String, String> kafkaErrorTemplate;

    public void sendMessage(String message) {
        kafkaErrorTemplate.send(ErrorProducerConfig.TOPIC, message);
    }
}
