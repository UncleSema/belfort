package ru.ct.belfort.kafka.producers;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.ct.belfort.TradingInfoDTO;

import java.util.Map;

// This class is for testing. It should take place in tinkoff-service

@Configuration
public class TestCandlesProducerConfig {

    @Bean
    public ProducerFactory<String, TradingInfoDTO> testCandlesProducerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers
    ) {
        return new DefaultKafkaProducerFactory<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        ));
    }

    @Bean
    public KafkaTemplate<String, TradingInfoDTO> testCandlesProducerTemplate(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers
    ) {
        return new KafkaTemplate<>(testCandlesProducerFactory(bootstrapServers));
    }
}
