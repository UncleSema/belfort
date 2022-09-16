package ru.ct.belfort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ct.belfort.producer.CandlesProducer;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class CandleSubscriptionService {

    private static CandlesProducer candlesProducer;
    private static Map<String, InvestApi> api;

    @Autowired
    public CandleSubscriptionService(CandlesProducer candlesProducer) {
        CandleSubscriptionService.candlesProducer = candlesProducer;
        api = new HashMap<>();
    }

    private static InvestApi getApiByToken(String token) {
        if (api.containsKey(token)) {
            return api.get(token);
        } else {
            var currentApi = InvestApi.create(token);
            api.put(token, currentApi);
            return currentApi;
        }
    }

    public static void subscribe(String token, List<String> figis) {
        CandleSubscriber subs = new CandleSubscriber();
        subs.setProducer(candlesProducer);
        Consumer<Throwable> onErrorCallback = error -> System.err.println(error.toString());
        var currentApi = getApiByToken(token);
        var subsService =
                currentApi.getMarketDataStreamService().newStream("candles_stream", subs, onErrorCallback);
        subsService.subscribeCandles(figis);
    }

    public static void unsubscribe(String token, List<String> figis) {
        //TODO: if stream has no figis, should I delete InvestAPI from map?
        var currentApi = getApiByToken(token);
        var subsService = currentApi.
                getMarketDataStreamService().
                getStreamById("candles_stream");
        subsService.unsubscribeCandles(figis);
    }
}
