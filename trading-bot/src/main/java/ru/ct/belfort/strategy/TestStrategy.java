package ru.ct.belfort.strategy;

import ru.ct.belfort.TradingInfoDTO;

import java.util.Random;

public class TestStrategy {

    public static double predict(TradingInfoDTO info) {
        Random random = new Random();
        return random.nextDouble() * 100;
    }
}
