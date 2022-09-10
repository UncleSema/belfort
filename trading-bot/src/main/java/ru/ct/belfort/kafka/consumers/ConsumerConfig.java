package ru.ct.belfort.kafka.consumers;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import ru.ct.belfort.kafka.KafkaConfig;

import java.util.Map;

import static ru.ct.belfort.kafka.KafkaConfig.KAFKA_BOOTSTRAP_ADDRESS;

@EnableKafka
@Configuration
public class ConsumerConfig {

    private static final String groupId = "trading_bot_consumers";

    @Bean
    public ConsumerFactory<String, String> consumerFactory(
            @Value(value = KAFKA_BOOTSTRAP_ADDRESS) String bootstrapAddress
    ) {
        return new DefaultKafkaConsumerFactory<>(Map.of(
                org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, groupId,
                org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class
        ));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(KafkaConfig.KAFKA_BOOTSTRAP_ADDRESS));
        return factory;
    }
}