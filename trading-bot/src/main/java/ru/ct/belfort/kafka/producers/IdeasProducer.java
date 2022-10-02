package ru.ct.belfort.kafka.producers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.ct.belfort.IdeaDTO;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdeasProducer {

    KafkaTemplate<String, IdeaDTO> kafkaTemplate;
    KafkaTemplate<String, String> kafkaErrorTemplate;

    private <K, V> void addCallback(ListenableFuture<SendResult<K, V>> future, V message) {
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<K, V> result) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendMessage(IdeaDTO message) {

        ListenableFuture<SendResult<String, IdeaDTO>> future =
                kafkaTemplate.send(IdeasProducerConfig.IDEAS_TOPIC, message);
        addCallback(future, message);
    }

    public void sendError(String message) {

        ListenableFuture<SendResult<String, String>> future =
                kafkaErrorTemplate.send(IdeasProducerConfig.ERROR_TOPIC, message);
        addCallback(future, message);
    }
}
