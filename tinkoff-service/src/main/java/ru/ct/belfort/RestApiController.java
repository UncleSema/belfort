package ru.ct.belfort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ct.belfort.producer.TestProducer;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RestApiController {

    @NonNull
    private final TestProducer producer;

    @GetMapping("/tinkoff-service")
    public String hello() {
        return "Tinkoff service is working!";
    }

    @GetMapping("/tinkoff-api-test")
    public String tinkoff() {
        producer.sendMessage(new UserDTO(1,
                "put-your-token-here",
                "someStrategy",
                List.of("put-your-figi-here")));
        return "Tinkoff api is working!";
    }
}
