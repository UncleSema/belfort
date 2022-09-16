package ru.ct.belfort.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.tinkoff.piapi.contract.v1.Candle;

import java.util.Map;

import static ru.ct.belfort.KafkaConfig.bootstrapAddress;

@Configuration
public class CandlesProducerConfig {
    @Bean
    public ProducerFactory<String, Candle> CandlesProducerFactory() {
        return new DefaultKafkaProducerFactory<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        ));
    }
    @Bean
    public KafkaTemplate<String, Candle> CandlesProducerTemplate() {
        return new KafkaTemplate<>(CandlesProducerFactory());
    }
}
