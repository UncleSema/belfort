package ru.ct.belfort.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.ct.belfort.IdeaDTO;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.kafka.consumers.CandlesConsumerConfig;
import ru.ct.belfort.kafka.producers.IdeasProducerConfig;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static ru.ct.belfort.Utils.closePricesToCandles;

@Testcontainers
@SpringBootTest
public class KafkaTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    public void sendCandles() throws Exception {

        JsonDeserializer<IdeaDTO> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");

        KafkaProducer<String, TradingInfoDTO> producer = new KafkaProducer<>(
                Map.of(
                        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers(),
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                        ProducerConfig.CLIENT_ID_CONFIG, "candles_test_producer"
                ),
                new StringSerializer(),
                new JsonSerializer<>()
        );

        KafkaConsumer<String, IdeaDTO> consumer = new KafkaConsumer<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers(),
                        ConsumerConfig.GROUP_ID_CONFIG, "ideas_test_consumer",
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                ),
                new StringDeserializer(),
                jsonDeserializer
        );

        KafkaConsumer<String, String> errorConsumer = new KafkaConsumer<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers(),
                        ConsumerConfig.GROUP_ID_CONFIG, "error_test_consumer",
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                ),
                new StringDeserializer(),
                new StringDeserializer()
        );

        // IDK why, but without waiting producer.send() doesn't do anything
        Thread.sleep(1000);

        final var message = new TradingInfoDTO(
                closePricesToCandles(new double[]{500, 493, 491, 485, 483, 482, 480, 467, 463, 461}),
                "rsi"
        );

        consumer.subscribe(Collections.singletonList(IdeasProducerConfig.TOPIC));
        producer.send(new ProducerRecord<>(CandlesConsumerConfig.TOPIC, message));

        Unreliables.retryUntilTrue(
                5, TimeUnit.SECONDS,
                () -> {
                    ConsumerRecords<String, IdeaDTO> records = consumer.poll(Duration.ofMillis(100));
                    if (records.isEmpty()) {
                        return false;
                    }
                    assertThat(records)
                            .hasSize(1)
                            .extracting(ConsumerRecord::topic, ConsumerRecord::value)
                            .containsExactly(tuple(IdeasProducerConfig.TOPIC, new IdeaDTO(0.0, "Recommended to buy")));

                    return true;
                }
        );

        consumer.unsubscribe();
    }
}
