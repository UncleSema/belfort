package ru.ct.belfort.strategy;

import org.springframework.stereotype.Component;
import ru.ct.belfort.CandleDTO;

import java.util.List;
import java.util.Random;

@Component
public class TestStrategy implements Strategy {

    public double predict(List<CandleDTO> info) {
        Random random = new Random();
        return random.nextDouble() * 100;
    }
}
