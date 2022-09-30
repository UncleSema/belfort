package ru.ct.belfort.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.ct.belfort.CandleDTO;

@Slf4j
@Service
public class CandlesProducer {
    private final KafkaTemplate<String, CandleDTO> candlesProducer;
    @Autowired
    public CandlesProducer(@Qualifier("CandlesProducerTemplate") KafkaTemplate<String, CandleDTO> candlesProducer) {
        this.candlesProducer = candlesProducer;
    }

    public void sendMessage(CandleDTO message) {
        ListenableFuture<SendResult<String, CandleDTO>> future =
                candlesProducer.send("ct.belfort.invest.candles", message);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, CandleDTO> result) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
    }
}
