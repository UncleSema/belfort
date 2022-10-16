package ru.ct.belfort.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.CandleSubscriptionService;
import ru.ct.belfort.OperationSubscriptionService;
import ru.ct.belfort.UserDTO;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersConsumer {

    private final CandleSubscriptionService candleSubscriptionService;
    private final OperationSubscriptionService operationSubscriptionService;

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