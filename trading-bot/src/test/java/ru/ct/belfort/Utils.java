package ru.ct.belfort;

import org.testcontainers.shaded.com.google.common.primitives.Doubles;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Utils {

    private static final Random random = new Random();

    public static List<CandleDTO> closePricesToCandles(double[] closePrices) {
        if (closePrices.length == 0) {
            return List.of();
        }
        var min = Arrays.stream(closePrices).min().getAsDouble();
        var max = Arrays.stream(closePrices).max().getAsDouble();
        var candles = Arrays.stream(closePrices)
                .mapToObj(price -> new CandleDTO(min, max, min, price, 10));
        return candles.toList();
    }

    public static List<CandleDTO> genRandomCandles(int amount, double minClosePrice, double maxClosePrice) {
        assert amount <= 1_000_000;
        assert minClosePrice > 0;
        assert maxClosePrice >= minClosePrice;
        assert maxClosePrice < 1e12;

        var closePrices = Stream
                .generate(() -> random.nextDouble() * (maxClosePrice - minClosePrice) + minClosePrice)
                .limit(amount)
                .toList();
        return closePricesToCandles(Doubles.toArray(closePrices));
    }

    public static TradingInfoDTO genRandomTradingInfoDTO(String strategy) {
        var candles = genRandomCandles(10, 5, 10);
        return new TradingInfoDTO(candles, strategy);
    }
}
