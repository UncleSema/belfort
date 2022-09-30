package ru.ct.belfort.util;

import ru.ct.belfort.CandleDTO;
import ru.tinkoff.piapi.contract.v1.Candle;

import java.math.BigDecimal;

public class Utilities {
    public static CandleDTO create(Candle dto) {
        return new CandleDTO(
                makeDouble(dto.getLow().getUnits(), dto.getLow().getNano()),
                makeDouble(dto.getHigh().getUnits(), dto.getHigh().getNano()),
                makeDouble(dto.getOpen().getUnits(), dto.getOpen().getNano()),
                makeDouble(dto.getClose().getUnits(), dto.getClose().getNano()),
                dto.getVolume());
    }

    public static double makeDouble(long units, int nano) {
        BigDecimal bigDecimal = units == 0 && nano == 0 ?
                BigDecimal.ZERO :
                BigDecimal.valueOf(units).add(BigDecimal.valueOf(nano, 9));
        return bigDecimal.doubleValue();
    }
}
