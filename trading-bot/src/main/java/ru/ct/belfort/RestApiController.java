package ru.ct.belfort;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ct.belfort.kafka.producers.TestCandlesProducer;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/trading")
@RequiredArgsConstructor
public class RestApiController {

    private final TestCandlesProducer testCandlesProducer;

    @GetMapping("/hello")
    public String hello() {
        return "Trading bot is working!";
    }

    // Testing that Kafka works. It will be here before integration tests appear
    @GetMapping("/test-strat")
    public String testStrategy() {
        TradingInfoDTO info = new TradingInfoDTO(List.of(), "test");
        testCandlesProducer.sendMessage(info);
        return "Check console";
    }

    // Testing that Kafka works. It will be here before integration tests appear
    @GetMapping("/rsi-strat")
    public String rsiStrategy() {
        double[] goodSample = {20, 21, 25, 23, 27, 26, 28, 28, 31, 29};
        CandleDTO[] candles = new CandleDTO[goodSample.length];
        for (int i = 0; i < goodSample.length; i++) {
            candles[i] = new CandleDTO(0, goodSample[i] * 200, 0, goodSample[i] * 100, 0);
        }
        TradingInfoDTO info = new TradingInfoDTO(Arrays.asList(candles), "rsi");
        testCandlesProducer.sendMessage(info);
        return "Check console";
    }

}
