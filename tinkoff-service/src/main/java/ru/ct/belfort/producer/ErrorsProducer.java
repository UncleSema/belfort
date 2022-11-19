package ru.ct.belfort.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ErrorsProducer {
    private final KafkaTemplate<String, String> errorsProducer;

    @Autowired
    public ErrorsProducer(@Qualifier("ErrorsProducerTemplate") KafkaTemplate<String, String> errorsProducer) {
        this.errorsProducer = errorsProducer;
    }

    public void sendMessage(String message) {
        errorsProducer.send("ct.belfort.invest.errors", message);
    }
}
