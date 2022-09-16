package ru.ct.belfort;

import ru.ct.belfort.producer.CandlesProducer;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

public class CandleSubscriber implements StreamProcessor<MarketDataResponse> {

    private CandlesProducer producer;

    @Override
    public void process(MarketDataResponse response) {
        if (response.hasCandle()) {
            System.out.println("New candle!");
            System.out.println(response.getCandle());

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
