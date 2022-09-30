package ru.ct.belfort.strategy;

import ru.ct.belfort.CandleDTO;

import java.util.List;

public interface StrategyInterface {
    double predict(List<CandleDTO> info);
    String getQualifier();
}