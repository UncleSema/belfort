import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import ru.ct.belfort.CandleDTO;
import ru.ct.belfort.producer.CandlesProducer;

import static org.mockito.ArgumentMatchers.any;

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
}




