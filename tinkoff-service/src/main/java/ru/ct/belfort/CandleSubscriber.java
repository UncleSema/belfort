package ru.ct.belfort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ct.belfort.producer.CandlesProducer;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

public class CandleSubscriber implements StreamProcessor<MarketDataResponse> {
    private static final Logger log = LoggerFactory.getLogger(CandleSubscriber.class);
    private CandlesProducer producer;

    @Override
    public void process(MarketDataResponse response) {
        if (response.hasCandle()) {
            log.info("New candle!");
            log.info(response.getCandle().toString());

            /*
            If you uncomment this, then you will have a grpc error :(
            * */

            //producer.sendMessage(response.getCandle());
        }
    }

    public void setProducer(CandlesProducer producer) {
        this.producer = producer;
    }
}