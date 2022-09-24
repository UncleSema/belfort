//package ru.ct.belfort.Kafka.Producers;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//import ru.ct.belfort.UserDTO;
//
//import java.util.Map;
//
//@Configuration
//public class TestProducerConfig {
//
//    @Value(value = "${kafka.bootstrapAddress}")
//    private String bootstrapAddress;
//    @Bean
//    public ProducerFactory<String, UserDTO> TestProducerFactory() {
//        return new DefaultKafkaProducerFactory<>(Map.of(
//                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
//                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
//                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
//        ));
//    }
//    @Bean
//    public KafkaTemplate<String, UserDTO> TestProducerTemplate() {
//        return new KafkaTemplate<>(TestProducerFactory());
//    }
//}
