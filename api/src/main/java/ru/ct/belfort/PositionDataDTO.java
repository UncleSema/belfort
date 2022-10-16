package ru.ct.belfort;

import java.util.List;

public record PositionDataDTO(String accountId, List<MoneyValueDTO> money, List<PositionsSecuritiesDTO> securities) {
}