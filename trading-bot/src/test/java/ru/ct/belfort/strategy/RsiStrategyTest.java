package ru.ct.belfort.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ct.belfort.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.ct.belfort.Utils.genRandomCandles;

class RsiStrategyTest {

    private final static Random random = new Random();
    private final static RsiStrategy strategy = new RsiStrategy();

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
                .map(Utils::closePricesToCandles)
                .mapToDouble(strategy::predict)
                .toArray();

        for (int i = 0; i < testcases.size() - 1; i++) {
            Assertions.assertTrue(results[i + 1] - results[i] > -1e6);
        }
    }
}