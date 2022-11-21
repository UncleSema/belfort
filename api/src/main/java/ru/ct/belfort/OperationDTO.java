package ru.ct.belfort;

public record OperationDTO(String id, MoneyValueDTO payment, MoneyValueDTO price, String figi, Advice operationType) {
}
