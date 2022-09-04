package ru.ct.belfort;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @GetMapping("/telegram-service")
    public String service() {
        return "Telegram service is added!";
    }
}
