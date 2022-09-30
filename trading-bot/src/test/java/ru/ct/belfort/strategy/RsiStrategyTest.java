package ru.ct.belfort.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ct.belfort.CandleDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class RsiStrategyTest {

    private final static Random random = new Random();
    private final static RsiStrategy strategy = new RsiStrategy();

    private static List<CandleDTO> closePricesToCandles(double[] closePrices) {
        var min = Arrays.stream(closePrices).min().getAsDouble();
        var max = Arrays.stream(closePrices).max().getAsDouble();
        CandleDTO[] candles = new CandleDTO[closePrices.length];
        for (int i = 0; i < closePrices.length; i++) {
            //TODO: change it!
//            candles[i] = new CandleDTO(min, max, min, closePrices[i], 10);
        }
        return Arrays.asList(candles);
    }

    private static List<CandleDTO> genRandomCandles(int amount, double minClosePrice, double maxClosePrice) {
        assert amount <= 1_000_000;
        assert minClosePrice > 0;
        assert maxClosePrice >= minClosePrice;
        assert maxClosePrice < 1e12;

        double[] closePrices = new double[amount];
        for (int i = 0; i < amount; i++) {
            closePrices[i] = random.nextDouble() * (maxClosePrice - minClosePrice) + minClosePrice;
            assert closePrices[i] >= minClosePrice;
            assert closePrices[i] <= maxClosePrice;
        }

        return closePricesToCandles(closePrices);
    }

    @Test
    void randomTest() {
        for (int len = 10; len <= 1000; len += 10) {
            var minClosePrice = random.nextDouble() * 100;
            var maxClosePrice = (1 + 10 * random.nextDouble()) * minClosePrice;
            var result = strategy.predict(genRandomCandles(len, minClosePrice, maxClosePrice));
            Assertions.assertTrue(result >= 0 && result <= 100);
        }
    }

    @Test
    void intelligenceTest() {
        List<double[]> testcases = new ArrayList<>();

        testcases.add(new double[]{500, 493, 491, 485, 483, 482, 480, 467, 463, 461}); // strictly descending
        testcases.add(new double[]{500, 493, 493, 491, 485, 485, 483, 483, 472, 465}); // descending
        testcases.add(new double[]{500, 502, 493, 493, 489, 483, 487, 482, 477, 478}); // mostly descending
        testcases.add(new double[]{500, 500, 500, 500, 500, 500, 500, 500, 500, 500}); // stable
        testcases.add(new double[]{500, 502, 511, 506, 513, 520, 515, 515, 523, 534}); // mostly ascending
        testcases.add(new double[]{500, 503, 503, 508, 512, 512, 525, 527, 535, 544}); // ascending
        testcases.add(new double[]{500, 505, 517, 523, 524, 525, 536, 541, 547, 555}); // strictly ascending

        var results = testcases
                .stream()
                .map(RsiStrategyTest::closePricesToCandles)
                .mapToDouble(strategy::predict)
                .toArray();

        for (int i = 0; i < testcases.size() - 1; i++) {
            Assertions.assertTrue(results[i + 1] - results[i] > -1e6);
        }
    }
}