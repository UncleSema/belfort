package ru.ct.belfort.kafka.producers;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.ct.belfort.TradingInfoDTO;

import java.util.Map;

import static ru.ct.belfort.kafka.KafkaConfig.KAFKA_BOOTSTRAP_ADDRESS;

// This class is for testing. It should take place in tinkoff-service

@Configuration
public class TestCandlesProducerConfig {

    @Bean
    public ProducerFactory<String, TradingInfoDTO> TestCandlesProducerFactory() {
        return new DefaultKafkaProducerFactory<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_ADDRESS,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        ));
    }

    @Bean
    public KafkaTemplate<String, TradingInfoDTO> TestCandlesProducerTemplate() {
        return new KafkaTemplate<>(TestCandlesProducerFactory());
    }
}
