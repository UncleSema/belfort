package ru.ct.belfort.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.tinkoff.piapi.contract.v1.Candle;

@Service
public class CandlesProducer {
    private final KafkaTemplate<String, Candle> candlesProducer;
    private static final Logger log = LoggerFactory.getLogger(CandlesProducer.class);
    @Autowired
    public CandlesProducer(@Qualifier("CandlesProducerTemplate") KafkaTemplate<String, Candle> candlesProducer) {
        this.candlesProducer = candlesProducer;
    }

    public void sendMessage(Candle message) {
        ListenableFuture<SendResult<String, Candle>> future =
                candlesProducer.send("ct.belfort.invest.candles", message);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, Candle> result) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
    }
}
