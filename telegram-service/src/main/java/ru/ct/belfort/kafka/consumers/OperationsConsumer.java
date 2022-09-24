package ru.ct.belfort.kafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.UserDTO;

@Service
public class OperationsConsumer {
    @KafkaListener(topics = "ct.belfort.invest.operations", groupId = "telegram-service-consumers")
    public void consume(ConsumerRecord<String, UserDTO> record) {
        System.out.println("Operations consumer works!");
        System.out.println("key=" + record.key());
        System.out.println("value=" + record.value());
    }
}
