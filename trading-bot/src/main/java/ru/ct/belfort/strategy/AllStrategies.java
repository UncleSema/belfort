package ru.ct.belfort.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ct.belfort.CandleDTO;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public final class AllStrategies {

    public final TestStrategy test;
    public final RsiStrategy rsi;

    public interface Strategy {
        double predict(List<CandleDTO> info);
        String getQualifier();
    }

    @Component
    public static class TestStrategy implements Strategy {

        public double predict(List<CandleDTO> info) {
            Random random = new Random();
            return random.nextDouble() * 100;
        }

        @Override
        public String getQualifier() {
            return "test";
        }
    }

    @Component
    public static class RsiStrategy implements Strategy {

        private static final double EPS = 1e-6;

        public double predict(List<CandleDTO> candles) {
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
}
