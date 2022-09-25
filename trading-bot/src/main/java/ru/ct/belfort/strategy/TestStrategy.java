package ru.ct.belfort.strategy;

import ru.ct.belfort.CandleDTO;

import java.util.List;
import java.util.Random;

@Strategy
public class TestStrategy implements StrategyInterface {

    public double predict(List<CandleDTO> info) {
        Random random = new Random();
        return random.nextDouble() * 100;
    }

    @Override
    public String getQualifier() {
        return "test";
    }
}