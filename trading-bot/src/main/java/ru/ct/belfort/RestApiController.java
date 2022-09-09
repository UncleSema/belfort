package ru.ct.belfort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ct.belfort.kafka.producers.IdeasProducer;

@RestController
@RequestMapping("/trading")
public class RestApiController {

    @GetMapping("/hello")
    public String hello() {
        return "Trading bot is working!";
    }

    @GetMapping("/testkafka")
    public String testKafka(@Autowired IdeasProducer producer) {
        producer.sendMessage("Kafka is working!");
        return "Check console";
    }
}
