package ru.ct.belfort;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ct.belfort.client.TinkoffClient;
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

    public void subscribe(TinkoffClient client, List<String> figis) {
        log.info("New subscribe");
        CandleSubscriber subs = new CandleSubscriber(candlesProducer);
        Consumer<Throwable> onErrorCallback = error -> log.error(error.toString());
        client.subscribeCandles(figis, subs, onErrorCallback);
    }

    public void unsubscribe(TinkoffClient client, List<String> figis) {
        log.info("unsubscribe");
        client.unsubscribeCandles(figis);
    }
}
