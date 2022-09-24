package ru.ct.belfort.strategy;

import java.util.Random;

public class TestStrategy implements Strategy {

    public static double predict() {
        Random random = new Random();
        return random.nextDouble() * 100;
    }
}
