package ru.ct.belfort.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.ct.belfort.UserDTO;

@Service
public class TestProducer {
    private final KafkaTemplate<String, UserDTO> testProducer;

    @Autowired
    public TestProducer(@Qualifier("TestProducerTemplate") KafkaTemplate<String, UserDTO> candlesProducer) {
        this.testProducer = candlesProducer;
    }

    public void sendMessage(UserDTO message) {
        ListenableFuture<SendResult<String, UserDTO>> future =
                testProducer.send("ct.belfort.telegram.users", message);

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
