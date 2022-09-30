package ru.ct.belfort.kafka.producers;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.ct.belfort.UserDTO;

import java.util.Map;

@Configuration
public class UsersProducerConfig {
    @Bean
    public ProducerFactory<String, UserDTO> userProducerFactory(
            @Value(value = "${kafka.bootstrapAddress}") String bootstrapAddress) {
        return new DefaultKafkaProducerFactory<>(Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress, ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class, ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class));
    }

    @Bean
    public KafkaTemplate<String, UserDTO> userKafkaTemplate(
            @Qualifier("userProducerFactory") ProducerFactory<String, UserDTO> userProdFactory) {
        return new KafkaTemplate<>(userProdFactory);
    }
}
