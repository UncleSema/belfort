import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import ru.ct.belfort.CandleDTO;
import ru.ct.belfort.producer.CandlesProducer;
import ru.ct.belfort.subscribers.CandleSubscriber;
import ru.ct.belfort.util.Utilities;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import static org.mockito.ArgumentMatchers.any;
import static ru.ct.belfort.util.Utilities.makeQuotation;

@ExtendWith(MockitoExtension.class)
public class CandlesProducerTest {
    @Test
    void sendCandlesTest() {
        @SuppressWarnings("unchecked")
        KafkaTemplate<String, CandleDTO> kafkaTemplate = Mockito.mock(KafkaTemplate.class);

        @SuppressWarnings("unchecked")
        ListenableFuture<SendResult<String, CandleDTO>> lf = Mockito.mock(ListenableFuture.class);
        Mockito.when(kafkaTemplate.send(any(String.class), any(CandleDTO.class)))
                .thenReturn(lf);

        CandlesProducer producer = new CandlesProducer(kafkaTemplate);


        CandleDTO dto1 = new CandleDTO(5, 6, 7, 8, 9);
        CandleDTO dto2 = new CandleDTO(15, 68, 79, 81, 92);
        CandleDTO dto3 = new CandleDTO(3125, 126, 732, 418, 9.9412);

        producer.sendMessage(dto1);
        producer.sendMessage(dto2);
        producer.sendMessage(dto3);

        Mockito.verify(kafkaTemplate, Mockito.times(1))
                .send("ct.belfort.invest.candles", dto1);
        Mockito.verify(kafkaTemplate, Mockito.times(1))
                .send("ct.belfort.invest.candles", dto2);
        Mockito.verify(kafkaTemplate, Mockito.times(1))
                .send("ct.belfort.invest.candles", dto3);
    }

    @Test
    void candleSubscriberTest() {
        CandlesProducer producer = Mockito.mock(CandlesProducer.class);
        CandleSubscriber subscriber = new CandleSubscriber(producer);

        Candle tinkoffCandle = Candle.newBuilder().
                setLow(
                        makeQuotation(23, 32)
                ).setHigh(
                        makeQuotation(111, 1323231214)
                ).setOpen(
                        makeQuotation(3123, 121231233)
                ).setClose(
                        makeQuotation(1231,312315)
                ).setVolume(
                        888
                ).build();

        MarketDataResponse response1 = MarketDataResponse
                .newBuilder()
                .setCandle(tinkoffCandle).build();

        subscriber.process(response1);
        //Checking that I'm sending candle if I got it
        Mockito.verify(producer, Mockito.times(1))
                .sendMessage(Utilities.create(tinkoffCandle));

        MarketDataResponse response2 =
                MarketDataResponse.newBuilder().build();
        subscriber.process(response2);

        //Checking that if I don't have any candles I don't send anything to kafka
        Mockito.verify(producer, Mockito.times(1))
                .sendMessage(any());
    }
}




