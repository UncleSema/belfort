package ru.ct.belfort.strategy;

import ru.ct.belfort.TradingInfoDTO;

public interface Strategy {

    // This method mustn't be called
    @Deprecated
    static double predict(TradingInfoDTO info) {
        return -1;
    }
}
