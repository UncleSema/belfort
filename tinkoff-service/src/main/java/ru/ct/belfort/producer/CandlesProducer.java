package ru.ct.belfort.producer;

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
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
