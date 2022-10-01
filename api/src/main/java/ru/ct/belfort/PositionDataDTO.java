package ru.ct.belfort;

import java.util.List;

public record PositionDataDTO(String account_id, List<MoneyValueDTO> money, List<PositionSecuritiesDTO> securities) {
}