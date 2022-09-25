package ru.ct.belfort.util;

import ru.ct.belfort.CandleDTO;
import ru.tinkoff.piapi.contract.v1.Candle;

public class Utilities {
    public static CandleDTO create(Candle dto) {
        return new CandleDTO(dto.getLow().getNano(),
                dto.getHigh().getNano(),
                dto.getOpen().getNano(),
                dto.getClose().getNano(),
                dto.getVolume());
    }
}
