package ru.ct.belfort;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @GetMapping("/tinkoff-service")
    public String hello() {
        return "Tinkoff service is working!";
    }
}
