package ru.ct.belfort.strategy;

import ru.ct.belfort.CandleDTO;

import java.util.List;

// Stolen from https://github.com/hondasmx/rsi_strategy

public class RsiStrategy implements Strategy {

    private static final double EPS = 1e-6;

    public static double predict(List<CandleDTO> candles) {
        var totalGain = 0.0;
        var totalLoss = 0.0;
        var gainCount = 0;
        var lossCount = 0;

        for (int i = 1; i < candles.size(); i++) {
            var candleClosePrice = candles.get(i).closePrice();
            var prevCandleClosePrice = candles.get(i - 1).closePrice();
            var diff = candleClosePrice - prevCandleClosePrice;
            if (Math.abs(diff) < EPS) {
                continue;
            }
            if (diff > 0) {
                totalGain += diff;
                gainCount++;
            } else {
                totalLoss -= diff;
                lossCount++;
            }
        }

        gainCount = (gainCount > 0 ? gainCount : 1);
        lossCount = (lossCount > 0 ? lossCount : 1);

        var avgGain = totalGain / gainCount;
        var avgLoss = totalLoss / lossCount;

        System.out.println(totalGain + " " + gainCount + " " + totalLoss + " " + lossCount);
        System.out.println(avgGain + " " + avgLoss);

        if (avgLoss < EPS) {
            return 100;
        }
        var rs = avgGain / avgLoss;
        return 100 - 100 / (1 + rs);
    }
}
