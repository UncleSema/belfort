package ru.ct.belfort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.ct.belfort.producer.CandlesProducer;
import ru.ct.belfort.util.Utilities;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

@RequiredArgsConstructor
@Slf4j
public class CandleSubscriber implements StreamProcessor<MarketDataResponse> {
    @NonNull
    private final CandlesProducer producer;

    @Override
    public void process(MarketDataResponse response) {
        if (response.hasCandle()) {
            log.info("New candle!");
            log.info(response.getCandle().toString());

            producer.sendMessage(Utilities.create(response.getCandle()));
        }
    }
}
