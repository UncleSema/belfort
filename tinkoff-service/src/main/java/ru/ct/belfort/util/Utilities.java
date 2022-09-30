package ru.ct.belfort.util;

import ru.ct.belfort.CandleDTO;
import ru.tinkoff.piapi.contract.v1.Candle;

public class Utilities {
    public static CandleDTO create(Candle dto) {
        return new CandleDTO(
                makeDouble(dto.getLow().getUnits(), dto.getLow().getNano()),
                makeDouble(dto.getHigh().getUnits(), dto.getHigh().getNano()),
                makeDouble(dto.getOpen().getUnits(), dto.getOpen().getNano()),
                makeDouble(dto.getClose().getUnits(), dto.getClose().getNano()),
                dto.getVolume());
    }

    public static double makeDouble(long firstPart, int secondPart) {
        double second = secondPart;
        while (second > 1) second /= 10.0;
        return firstPart + second;
    }
}
