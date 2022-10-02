package ru.ct.belfort.kafka.consumers;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Configuration
public class CandlesConsumerConfig {

    public static final String GROUP_ID = "candle_consumers";
    public static final String TOPIC = "ct.belfort.invest.candles";

    @Bean
    public ConsumerFactory<String, String> candlesConsumerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers
    ) {
        return new DefaultKafkaConsumerFactory<>(Map.of(
            org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
            org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID,
            org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            JsonDeserializer.TRUSTED_PACKAGES, "*"
        ));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> candlesConsumerContainerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(candlesConsumerFactory(bootstrapServers));
        return factory;
    }
}