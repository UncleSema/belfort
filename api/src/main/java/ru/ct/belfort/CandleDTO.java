package ru.ct.belfort;

import ru.tinkoff.piapi.contract.v1.Quotation;

public record CandleDTO(Quotation lowPrice, Quotation highPrice, Quotation openPrice, Quotation closePrice, double volume) {
}