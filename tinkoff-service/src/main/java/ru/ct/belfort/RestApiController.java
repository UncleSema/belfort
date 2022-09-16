package ru.ct.belfort;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ct.belfort.producer.TestProducer;

import java.util.List;


@RestController
public class RestApiController {

    private final TestProducer producer;

    public RestApiController(TestProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/tinkoff-service")
    public String hello() {
        return "Tinkoff service is working!";
    }

    @GetMapping("/tinkoff-api-test")
    public String tinkoff() {
        producer.sendMessage(new UserDTO(1,
                "t.ejIikFatmnI7NlPpuKdhTLgYbMtyo0XvNZqblOLR_LjiiJRnXdMvr0VSDVnerZcMlhBUxC_-Lfn7dRIhQEkv8Q",
                "someStrategy",
                List.of("BBG004730ZJ9")));
        return "Tinkoff api is working!";
    }
}
