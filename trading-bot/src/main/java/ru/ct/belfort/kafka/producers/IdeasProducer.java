package ru.ct.belfort.kafka.producers;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.ct.belfort.IdeaDTO;

@Service
@RequiredArgsConstructor
public class IdeasProducer {

    private final KafkaTemplate<String, IdeaDTO> kafkaTemplate;

    public void sendMessage(IdeaDTO message) {

        ListenableFuture<SendResult<String, IdeaDTO>> future =
                kafkaTemplate.send("ct.belfort.trade.ideas", message);

        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, IdeaDTO> result) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
