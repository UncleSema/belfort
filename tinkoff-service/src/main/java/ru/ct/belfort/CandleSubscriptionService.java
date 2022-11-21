package ru.ct.belfort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ct.belfort.client.TinkoffClientService;
import ru.ct.belfort.producer.CandlesProducer;
import ru.ct.belfort.subscribers.CandleSubscriber;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Service
public class CandleSubscriptionService {

    @NonNull
    private final CandlesProducer candlesProducer;
    private final TinkoffClientService tinkoffClientService;

    public void subscribe(String token, List<String> figis) {
        log.info("New subscribe");
        CandleSubscriber subs = new CandleSubscriber(candlesProducer);
        Consumer<Throwable> onErrorCallback = error -> log.error("Error while trying to subscribe on candles", error);
        tinkoffClientService.subscribeCandles(token, figis, subs, onErrorCallback);
    }

    public void unsubscribe(String token, List<String> figis) {
        log.info("unsubscribe");
        tinkoffClientService.unsubscribeCandles(token, figis);
    }
}
