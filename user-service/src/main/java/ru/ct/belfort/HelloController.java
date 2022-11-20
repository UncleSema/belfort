package ru.ct.belfort;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @ResponseBody
    @GetMapping("/greeting")
    public String getHelloWorld(@RequestParam(value="name", required = false, defaultValue = "world") String name) {
        return String.format("Hello, %s!", name);
    }
}
