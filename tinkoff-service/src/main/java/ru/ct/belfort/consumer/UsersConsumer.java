package ru.ct.belfort.consumer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.CandleSubscriptionService;
import ru.ct.belfort.UserDTO;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersConsumer {
    @NonNull
    private final CandleSubscriptionService service;

    @KafkaListener(topics = "ct.belfort.telegram.users",
            groupId = "tinkoff_service_consumers",
            containerFactory = "UsersConsumerContainerFactory")
    public void consume(ConsumerRecord<String, UserDTO> record) {
        log.info("Message is delivered!");
        UserDTO dto = record.value();
        service.subscribe(dto.token(), dto.figis());
    }
}
