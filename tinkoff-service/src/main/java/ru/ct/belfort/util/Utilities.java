package ru.ct.belfort.util;

import ru.ct.belfort.CandleDTO;
import ru.ct.belfort.MoneyValueDTO;
import ru.ct.belfort.PositionDataDTO;
import ru.ct.belfort.PositionsSecuritiesDTO;
import ru.tinkoff.piapi.contract.v1.*;

import java.math.BigDecimal;
import java.util.List;

public class Utilities {
    public static CandleDTO create(Candle dto) {
        return new CandleDTO(
                makeDouble(dto.getLow().getUnits(), dto.getLow().getNano()),
                makeDouble(dto.getHigh().getUnits(), dto.getHigh().getNano()),
                makeDouble(dto.getOpen().getUnits(), dto.getOpen().getNano()),
                makeDouble(dto.getClose().getUnits(), dto.getClose().getNano()),
                dto.getVolume());
    }

    public static PositionDataDTO create(PositionData dto) {
        List<MoneyValueDTO> moDTO =
                dto.getMoneyList()
                        .stream()
                        .map(s -> create(s.getAvailableValue()))
                        .toList();
        List<PositionsSecuritiesDTO> secDTO = dto.getSecuritiesList()
                .stream()
                .map(Utilities::create)
                .toList();
        return new PositionDataDTO(
                dto.getAccountId(),
                moDTO,
                secDTO
        );
    }

    public static PositionsSecuritiesDTO create(PositionsSecurities dto) {
        return new PositionsSecuritiesDTO(
                dto.getFigi(),
                dto.getPositionUid(),
                dto.getInstrumentUid(),
                dto.getInstrumentType());
    }

    public static MoneyValueDTO create(MoneyValue dto) {
        return new MoneyValueDTO(dto.getUnits(), dto.getNano());
    }

    public static double makeDouble(long units, int nano) {
        BigDecimal bigDecimal = units == 0 && nano == 0 ?
                BigDecimal.ZERO :
                BigDecimal.valueOf(units).add(BigDecimal.valueOf(nano, 9));
        return bigDecimal.doubleValue();
    }

    public static double makeDouble(Quotation quotation) {
        return makeDouble(quotation.getUnits(), quotation.getNano());
    }

    public static Quotation makeQuotation(long units, int nano) {
        return Quotation.newBuilder().
                setUnits(units).
                setNano(nano).build();
    }
}