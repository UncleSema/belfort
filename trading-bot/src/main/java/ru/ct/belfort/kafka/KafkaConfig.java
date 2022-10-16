package ru.ct.belfort.kafka;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:kafka.properties")
public class KafkaConfig {
}
