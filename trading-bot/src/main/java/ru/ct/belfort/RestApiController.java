package ru.ct.belfort;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trading")
@RequiredArgsConstructor
public class RestApiController {

    @GetMapping("/hello")
    public String hello() {
        return "Trading bot is working!";
    }

}
