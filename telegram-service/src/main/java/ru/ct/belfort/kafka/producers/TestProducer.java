package ru.ct.belfort.kafka.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;
import ru.ct.belfort.UserDTO;

@RestController
public class TestProducer {
    private final KafkaTemplate<String, UserDTO> testProducer;

    @Autowired
    public TestProducer(@Qualifier("TestProducerKafkaTemplate") KafkaTemplate<String, UserDTO> candlesProducer) {
        this.testProducer = candlesProducer;
    }

    public void sendMessage(UserDTO message) {
        testProducer.send("ct.belfort.invest.operations", message);
    }
}
