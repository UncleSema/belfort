package ru.ct.belfort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ct.belfort.producer.TestProducer;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class RestApiController {

    @NonNull
    private final TestProducer producer;

    private final String TINKOFF_TOKEN = System.getenv("tinkoff_token");

    @GetMapping("/tinkoff-service")
    public String hello() {
        return "Tinkoff service is working! Token=" + TINKOFF_TOKEN;
    }
    @GetMapping("/tinkoff-api-test")
    public String tinkoff() {
        producer.sendMessage(new UserDTO(1,
                TINKOFF_TOKEN,
                "someStrategy",
                List.of("BBG004730ZJ9")));
        return "Tinkoff api is working!";
    }
}