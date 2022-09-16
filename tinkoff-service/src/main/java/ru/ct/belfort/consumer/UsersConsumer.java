package ru.ct.belfort.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.CandleSubscriptionService;
import ru.ct.belfort.UserDTO;


@Service
public class UsersConsumer {
    private final CandleSubscriptionService service;
    private static final Logger log = LoggerFactory.getLogger(UsersConsumer.class);
    @Autowired
    public UsersConsumer(CandleSubscriptionService service) {
        this.service = service;
    }

    @KafkaListener(topics = "ct.belfort.telegram.users",
            groupId = "tinkoff_service_consumers",
            containerFactory = "UsersConsumerContainerFactory")
    public void consume(ConsumerRecord<String, UserDTO> record) {
        log.info("Message is delivered!");
        UserDTO dto = record.value();
        service.subscribe(dto.token(), dto.figis());
    }
}
