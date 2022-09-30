package ru.ct.belfort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ct.belfort.producer.CandlesProducer;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Service
public class CandleSubscriptionService {

    @NonNull
    private final CandlesProducer candlesProducer;
    private final Map<String, InvestApi> api = new HashMap<>();


    private InvestApi getApiByToken(String token) {
       return api.computeIfAbsent(token, InvestApi::create);
    }

    public void subscribe(String token, List<String> figis) {
        log.info("New subscribe");
        CandleSubscriber subs = new CandleSubscriber(candlesProducer);
        Consumer<Throwable> onErrorCallback = error -> System.err.println(error.toString());
        var currentApi = getApiByToken(token);
        var subsService =
                currentApi.getMarketDataStreamService().newStream("candles_stream", subs, onErrorCallback);
        subsService.subscribeCandles(figis);
    }

    public void unsubscribe(String token, List<String> figis) {
        log.info("unsubscribe");
        //TODO: if stream has no figis, should I delete InvestAPI from map?
        var currentApi = getApiByToken(token);
        var subsService = currentApi.
                getMarketDataStreamService().
                getStreamById("candles_stream");
        subsService.unsubscribeCandles(figis);
    }
}
