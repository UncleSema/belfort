package ru.ct.belfort.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static ru.ct.belfort.Utils.genRandomTradingInfoDTO;

@Testcontainers
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SpringBootTest(properties = "spring.main.lazy-initialization=true",
        classes = {TestCandlesProducer.class, TestCandlesProducerConfig.class})
@Component
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    public final TestCandlesProducer producer;

    // Doesn't work!
    // public final CandlesConsumer consumer;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    public void sendCandles() {
        /*JsonDeserializer<TradingInfoDTO> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");

        try (
            KafkaProducer<String, TradingInfoDTO> producer = new KafkaProducer<>(
                Map.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers(),
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                    ProducerConfig.CLIENT_ID_CONFIG, "pseudo_candles_producer"
                ),
                new StringSerializer(),
                new JsonSerializer<>()
            );

            KafkaConsumer<String, TradingInfoDTO> consumer = new KafkaConsumer<>(
                Map.of(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers(),
                    ConsumerConfig.GROUP_ID_CONFIG, CandlesConsumerConfig.GROUP_ID,
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                ),
                new StringDeserializer(),
                jsonDeserializer
            );
        ) {
            consumer.subscribe(Collections.singletonList(CandlesConsumerConfig.TOPIC));

            final var message = genRandomTradingInfoDTO("rsi");

            producer.send(new ProducerRecord<>(CandlesConsumerConfig.TOPIC, message));

            Unreliables.retryUntilTrue(
                10, TimeUnit.SECONDS,
                () -> {
                    ConsumerRecords<String, TradingInfoDTO> records = consumer.poll(Duration.ofMillis(100));
                    if (records.isEmpty()) {
                        return false;
                    }
                    assertThat(records)
                        .hasSize(1)
                        .extracting(ConsumerRecord::topic, ConsumerRecord::key, ConsumerRecord::value)
                        .containsExactly(tuple(CandlesConsumerConfig.TOPIC, "testcontainers", message));
                    return true;
                }
            );

            consumer.unsubscribe();
        }*/

        final var message = genRandomTradingInfoDTO("rsi");

        producer.sendMessage(message);

        Unreliables.retryUntilTrue(
                1, TimeUnit.SECONDS,
                () -> {
                    return false;
                    /*ConsumerRecords<String, TradingInfoDTO> records = consumer.poll(Duration.ofMillis(100));
                    if (records.isEmpty()) {
                        return false;
                    }
                    assertThat(records)
                            .hasSize(1)
                            .extracting(ConsumerRecord::topic, ConsumerRecord::key, ConsumerRecord::value)
                            .containsExactly(tuple(CandlesConsumerConfig.TOPIC, "testcontainers", message));
                    return true;*/
                }
        );
    }
}
