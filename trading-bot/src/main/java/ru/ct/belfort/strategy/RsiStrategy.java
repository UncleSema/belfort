package ru.ct.belfort.strategy;

import ru.ct.belfort.CandleDTO;

import java.util.List;

@Strategy
public class RsiStrategy implements StrategyInterface {

    private static final double EPS = 1e-6;

    public double predict(List<CandleDTO> candles) {
        var totalGain = 0.0;
        var totalLoss = 0.0;
        var gainCount = 0;
        var lossCount = 0;

        //TODO: change it!
//        for (int i = 1; i < candles.size(); i++) {
//            var candleClosePrice = candles.get(i).closePrice();
//            var prevCandleClosePrice = candles.get(i - 1).closePrice();
//            var diff = candleClosePrice - prevCandleClosePrice;
//            if (Math.abs(diff) < EPS) {
//                continue;
//            }
//            if (diff > 0) {
//                totalGain += diff;
//                gainCount++;
//            } else {
//                totalLoss -= diff;
//                lossCount++;
//            }
//        }

        if (gainCount == 0) {
            return lossCount == 0 ? 50 : 0;
        }
        if (lossCount == 0) {
            return 100;
        }

        var avgGain = totalGain / gainCount;
        var avgLoss = totalLoss / lossCount;

        var rs = avgGain / avgLoss;
        return 100 - 100 / (1 + rs);
    }

    @Override
    public String getQualifier() {
        return "rsi";
    }
}