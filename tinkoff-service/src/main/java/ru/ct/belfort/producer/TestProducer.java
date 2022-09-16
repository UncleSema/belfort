package ru.ct.belfort.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ct.belfort.UserDTO;

@Service
public class TestProducer {
    private final KafkaTemplate<String, UserDTO> testProducer;

    @Autowired
    public TestProducer(@Qualifier("TestProducerTemplate") KafkaTemplate<String, UserDTO> candlesProducer) {
        this.testProducer = candlesProducer;
    }

    public void sendMessage(UserDTO message) {
        testProducer.send("ct.belfort.telegram.users", message);
    }
}
