package ru.ct.belfort;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ct.belfort.kafka.producers.TestCandlesProducer;

import java.util.Arrays;
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

    @GetMapping("/test-strat")
    public String testStrategy() {
        TradingInfoDTO info = new TradingInfoDTO(List.of(), "test");
        testCandlesProducer.sendMessage(info);
        return "Check console";
    }

    @GetMapping("/rsi-strat")
    public String rsiStrategy() {
        CandleDTO[][] samples = new CandleDTO[3][10];

        // RSI uses only close price for calculations

        double[] badSample = {20, 19, 23, 14, 15, 12, 10, 10, 9, 6};
        double[] midSample = {20, 24, 23, 25, 21, 22, 22, 19, 17, 19};
        double[] goodSample = {20, 21, 25, 23, 27, 26, 28, 28, 31, 29};

        for (int j = 0; j < 10; j++) {
            samples[0][j] = new CandleDTO(0, 0, 0, badSample[j] * 100, 0);
            samples[1][j] = new CandleDTO(0, 0, 0, midSample[j] * 100, 0);
            samples[2][j] = new CandleDTO(0, 0, 0, goodSample[j] * 100, 0);
        }

        for (int testcase = 0; testcase < 3; testcase++) {
            TradingInfoDTO info = new TradingInfoDTO(Arrays.stream(samples[testcase]).toList(), "rsi");
            testCandlesProducer.sendMessage(info);
        }
        return "Check console";
    }

}
