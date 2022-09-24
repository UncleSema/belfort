package ru.ct.belfort;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ct.belfort.kafka.producers.TestProducer;

import java.util.List;

@RestController
public class RestApiController {

    private final TestProducer producer;

    public RestApiController(TestProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/telegram-service")
    public String service() {
        return "Telegram service is added!";
    }

    @GetMapping("/telegram-service-test")
    public String telegram() {
        producer.sendMessage(new UserDTO(1,
                "token",
                "strat",
                List.of("1")));
        return "Telegram service is working!";
    }


}
