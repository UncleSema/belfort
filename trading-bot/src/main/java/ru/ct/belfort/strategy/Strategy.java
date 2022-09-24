package ru.ct.belfort.strategy;

import ru.ct.belfort.CandleDTO;

import java.util.List;

public interface Strategy {
    double predict(List<CandleDTO> info);
}
