package ru.ct.belfort.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.UserDTO;

@Service
public class UsersConsumer {
    @KafkaListener(topics = "ct.belfort.telegram.users",
            groupId = "tinkoff_service_consumers",
            containerFactory = "UsersConsumerContainerFactory")
    public void consume(ConsumerRecord<String, UserDTO> record) {
        System.out.println("Read record!");
        System.out.println("key=" + record.key());
        System.out.println("value=" + record.value());
    }
}
