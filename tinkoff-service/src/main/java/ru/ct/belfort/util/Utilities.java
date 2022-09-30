package ru.ct.belfort.util;

import ru.ct.belfort.CandleDTO;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.Quotation;

public class Utilities {
    public static CandleDTO create(Candle dto) {
        return new CandleDTO(Quotation.newBuilder().
                setUnits(dto.getLow().getUnits()).
                setNano(dto.getLow().getNano()).build(),

                Quotation.newBuilder().
                setUnits(dto.getHigh().getUnits()).
                setNano(dto.getHigh().getNano()).build(),

                Quotation.newBuilder().
                setUnits(dto.getOpen().getUnits()).
                setNano(dto.getOpen().getNano()).build(),

                Quotation.newBuilder().
                setUnits(dto.getClose().getUnits()).
                setNano(dto.getClose().getNano()).build(),

                dto.getVolume());
    }
}
