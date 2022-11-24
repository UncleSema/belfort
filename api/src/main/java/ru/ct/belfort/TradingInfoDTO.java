package ru.ct.belfort;

import java.util.List;

public record TradingInfoDTO(List<CandleDTO> candles, String strategy, String figi) {
}
