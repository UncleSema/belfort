package ru.ct.belfort;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ct.belfort.kafka.producers.TestCandlesProducer;

import java.util.List;

@RestController
@RequestMapping("/trading")
public class RestApiController {

    private final TestCandlesProducer testCandlesProducer;

    public RestApiController(TestCandlesProducer testCandlesProducer) {
        this.testCandlesProducer = testCandlesProducer;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Trading bot is working!";
    }

    @GetMapping("/test/candles")
    public String testCandles() {
        CandleDTO candle1 = new CandleDTO(1, 3, 2, 2.5, 10);
        CandleDTO candle2 = new CandleDTO(20, 40, 30, 25, 1000);
        TradingInfoDTO info = new TradingInfoDTO(List.of(candle1, candle2), "test");
        testCandlesProducer.sendMessage(info);
        return "Check console";
    }

}
