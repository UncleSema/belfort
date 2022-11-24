package ru.ct.belfort.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.ct.belfort.IdeaDTO;

import java.util.Map;

import static ru.ct.belfort.KafkaConfig.bootstrapAddress;
import static ru.ct.belfort.KafkaConfig.consumersGroupId;

@Configuration
public class IdeasConsumerConfig {
    @Bean
    public ConsumerFactory<String, IdeaDTO> IdeasConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ConsumerConfig.GROUP_ID_CONFIG, consumersGroupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                JsonDeserializer.TRUSTED_PACKAGES, "*"
        ));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, IdeaDTO>
    IdeasConsumerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, IdeaDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(IdeasConsumerFactory());
        return factory;
    }
}
