package ru.ct.belfort.kafka.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import ru.ct.belfort.UserDTO;

@Service
public class UsersProducer {

    @Value(value = "${kafka.producerTopic}")
    String topicName;

    private final KafkaTemplate<String, UserDTO> kafkaTemplate;


    @Autowired
    public UsersProducer(@Qualifier("userKafkaTemplate") KafkaTemplate<String, UserDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

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
