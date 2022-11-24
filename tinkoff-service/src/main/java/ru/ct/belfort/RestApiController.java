package ru.ct.belfort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ct.belfort.client.TinkoffClientService;
import ru.ct.belfort.producer.TestProducer;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class RestApiController {

    @NonNull
    private final TestProducer producer;
    private final TinkoffClientService tinkoffClientService;

    private final String tinkoffToken = System.getenv("TINKOFF_TOKEN");

    @GetMapping("/tinkoff-service")
    public String hello() {
        return "Tinkoff service is working! Token=" + tinkoffToken;
    }
    @GetMapping("/tinkoff-api-test")
    public String sendMessage() {
        producer.sendMessage(new UserDTO(1,
                tinkoffToken,
                "someStrategy",
                List.of("BBG004730ZJ9")));
        return "Tinkoff api is working!";
    }

    @GetMapping("/tinkoff-api-buy")
    public String buy() {
        tinkoffClientService.buyByFigi(tinkoffToken, "BBG004730ZJ9");
        return "Trying to buy...!";
    }
    @GetMapping("/tinkoff-api-sell")
    public String sell() {
        tinkoffClientService.sellByFigi(tinkoffToken, "BBG004730ZJ9");
        return "Trying to sell...!";
    }
}