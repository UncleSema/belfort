package ru.ct.belfort;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {

    private static final Random random = new Random();

    public static List<CandleDTO> closePricesToCandles(double[] closePrices) {
        var min = Arrays.stream(closePrices).min().getAsDouble();
        var max = Arrays.stream(closePrices).max().getAsDouble();
        CandleDTO[] candles = new CandleDTO[closePrices.length];
        for (int i = 0; i < closePrices.length; i++) {
            candles[i] = new CandleDTO(min, max, min, closePrices[i], 10);
        }
        return Arrays.asList(candles);
    }

    public static List<CandleDTO> genRandomCandles(int amount, double minClosePrice, double maxClosePrice) {
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

    public static TradingInfoDTO genRandomTradingInfoDTO(String strategy) {
        var candles = genRandomCandles(10, 5, 10);
        return new TradingInfoDTO(candles, strategy);
    }
}
