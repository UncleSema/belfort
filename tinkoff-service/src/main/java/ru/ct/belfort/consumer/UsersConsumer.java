package ru.ct.belfort.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UsersConsumer {
    @KafkaListener(topics = "demo-topic",
            groupId = "tinkoff_service_consumers",
            containerFactory = "UsersConsumer")
    public void consume(ConsumerRecord<String, String> record) {
        //Want to use UserDTO as second parameter here
        System.out.println("Read record!");
        System.out.println("key=" + record.key());
        System.out.println("value=" + record.value());
    }
}
