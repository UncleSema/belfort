package ru.ct.belfort.kafka.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RestController;

import ru.ct.belfort.UserDTO;

@RestController
public class UsersProducer {

    @Value(value = "${kafka.producerTopic}")
    private String topicName;
    @Qualifier("UserKafkaTemplate")
    @Autowired
    private KafkaTemplate<String, UserDTO> kafkaTemplate;

    public void sendMessage(UserDTO message) {

        ListenableFuture<SendResult<String, UserDTO>> future =
                kafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, UserDTO> result) {
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
