package ru.ct.belfort.consumer;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.CandleSubscriptionService;
import ru.ct.belfort.OperationSubscriptionService;
import ru.ct.belfort.UserDTO;

@Slf4j
@Service
public class UsersConsumer {

    private final CandleSubscriptionService candleSubscriptionService;
    private final OperationSubscriptionService operationSubscriptionService;
    //TODO: can I do it simpler? Maybe with some annotations?
    @Autowired
    public UsersConsumer(CandleSubscriptionService candleSubscriptionService,
                         OperationSubscriptionService operationSubscriptionService) {
        this.candleSubscriptionService = candleSubscriptionService;
        this.operationSubscriptionService = operationSubscriptionService;
    }

    @KafkaListener(topics = "ct.belfort.telegram.users",
            groupId = "tinkoff_service_consumers",
            containerFactory = "UsersConsumerContainerFactory")
    public void consume(ConsumerRecord<String, UserDTO> record) {
        log.info("Message is delivered!");
        UserDTO dto = record.value();
        candleSubscriptionService.subscribe(dto.token(), dto.figis());
        operationSubscriptionService.subscribe(dto.token());
    }
}
