package ru.ct.belfort.kafka;

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

@Configuration
public class TestCandlesProducerConfig {

    @Bean
    public ProducerFactory<String, TradingInfoDTO> testCandlesProducerFactory2(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers
    ) {
        return new DefaultKafkaProducerFactory<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                ProducerConfig.CLIENT_ID_CONFIG, "TestCandlesProducer2"
        ));
    }

    @Bean
    public KafkaTemplate<String, TradingInfoDTO> testCandlesProducerTemplate2(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers
    ) {
        return new KafkaTemplate<>(testCandlesProducerFactory2(bootstrapServers));
    }
}
