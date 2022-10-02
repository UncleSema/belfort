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
import ru.ct.belfort.kafka.KafkaConfig;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdeasProducer {

    KafkaTemplate<String, IdeaDTO> kafkaTemplate;

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
                kafkaTemplate.send(KafkaConfig.IDEAS_TOPIC, message);
        addCallback(future, message);
    }
}
