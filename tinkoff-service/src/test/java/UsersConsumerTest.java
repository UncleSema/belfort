import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ct.belfort.CandleSubscriptionService;
import ru.ct.belfort.OperationSubscriptionService;
import ru.ct.belfort.UserDTO;
import ru.ct.belfort.client.TinkoffClient;
import ru.ct.belfort.client.TinkoffClientService;
import ru.ct.belfort.consumer.UsersConsumer;
import ru.ct.belfort.producer.ErrorsProducer;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UsersConsumerTest {
    @Test
    void userConsumerCorrectDataTest() {
        CandleSubscriptionService candleService = Mockito.mock(CandleSubscriptionService.class);
        OperationSubscriptionService operationService = Mockito.mock(OperationSubscriptionService.class);
        TinkoffClientService tinkoffClientService = Mockito.mock(TinkoffClientService.class);
        ErrorsProducer errorsProducer = Mockito.mock(ErrorsProducer.class);
        UsersConsumer consumer = new UsersConsumer(candleService, operationService, tinkoffClientService, errorsProducer);

        String correctToken = "correct_token";
        List<String> correctFigis = List.of("correct_figi1", "correct_figi2", "correct_figi3");

        UserDTO correctDto = new UserDTO(1, correctToken,
                "strategy",
                correctFigis);

        TinkoffClient client = Mockito.mock(TinkoffClient.class);

        Mockito.when(tinkoffClientService.getClientByToken(correctToken)).thenReturn(client);
        Mockito.when(client.isTokenValid()).thenReturn(true);
        Mockito.when(client.isListOfFigisValid(correctFigis)).thenReturn(true);
        ConsumerRecord<String, UserDTO> record =
                new ConsumerRecord<>("someTopic", 1, 1, "1", correctDto);
        consumer.consume(record);

        Mockito.verify(candleService, Mockito.times(1)).subscribe(client, correctFigis);
        Mockito.verify(operationService, Mockito.times(1)).subscribe(client);
        Mockito.verify(errorsProducer, Mockito.never()).sendMessage(Mockito.anyString());
    }

    @Test
    void userConsumerIncorrectTokenTest() {
        CandleSubscriptionService candleService = Mockito.mock(CandleSubscriptionService.class);
        OperationSubscriptionService operationService = Mockito.mock(OperationSubscriptionService.class);
        TinkoffClientService tinkoffClientService = Mockito.mock(TinkoffClientService.class);
        ErrorsProducer errorsProducer = Mockito.mock(ErrorsProducer.class);
        UsersConsumer consumer = new UsersConsumer(candleService, operationService, tinkoffClientService, errorsProducer);

        String incorrectToken = "incorrect_token";
        List<String> correctFigis = List.of("correct_figi1", "correct_figi2", "correct_figi3");
        UserDTO incorrectDto = new UserDTO(1, incorrectToken,
                "strategy",
                correctFigis);

        TinkoffClient client = Mockito.mock(TinkoffClient.class);

        Mockito.when(tinkoffClientService.getClientByToken(incorrectToken)).thenReturn(client);
        Mockito.when(client.isTokenValid()).thenReturn(false);

        ConsumerRecord<String, UserDTO> record =
                new ConsumerRecord<>("someTopic", 1, 1, "1", incorrectDto);
        consumer.consume(record);

        Mockito.verify(candleService, Mockito.never()).subscribe(client, correctFigis);
        Mockito.verify(operationService, Mockito.never()).subscribe(client);
        Mockito.verify(errorsProducer, Mockito.times(1)).sendMessage(Mockito.anyString());
    }

    @Test
    void userConsumerIncorrectFigisTest() {
        CandleSubscriptionService candleService = Mockito.mock(CandleSubscriptionService.class);
        OperationSubscriptionService operationService = Mockito.mock(OperationSubscriptionService.class);
        TinkoffClientService tinkoffClientService = Mockito.mock(TinkoffClientService.class);
        ErrorsProducer errorsProducer = Mockito.mock(ErrorsProducer.class);
        UsersConsumer consumer = new UsersConsumer(candleService, operationService, tinkoffClientService, errorsProducer);

        String correctToken = "correct_token";
        List<String> incorrectFigis = List.of("incorrect_figi1", "incorrect_figi2", "incorrect_figi3");

        UserDTO incorrectDto = new UserDTO(1, correctToken,
                "strategy",
                incorrectFigis);

        TinkoffClient client = Mockito.mock(TinkoffClient.class);

        Mockito.when(tinkoffClientService.getClientByToken(correctToken)).thenReturn(client);
        Mockito.when(client.isTokenValid()).thenReturn(true);
        Mockito.when(client.isListOfFigisValid(incorrectFigis)).thenReturn(false);
        ConsumerRecord<String, UserDTO> record =
                new ConsumerRecord<>("someTopic", 1, 1, "1", incorrectDto);
        consumer.consume(record);

        Mockito.verify(candleService, Mockito.never()).subscribe(client, incorrectFigis);
        Mockito.verify(operationService, Mockito.never()).subscribe(client);
        Mockito.verify(errorsProducer, Mockito.times(1)).sendMessage(Mockito.anyString());
    }
}
