package ru.ct.belfort.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.ct.belfort.TradingInfoDTO;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static ru.ct.belfort.Utils.genRandomTradingInfoDTO;

@RequiredArgsConstructor
@Testcontainers
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaTest {

    @Container
    KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    @Test
    public void sendCandles() {
        JsonDeserializer<TradingInfoDTO> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");

        try (
            KafkaProducer<String, TradingInfoDTO> producer = new KafkaProducer<>(
                Map.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers()
                ),
                new StringSerializer(),
                new JsonSerializer<>()
            );

            KafkaConsumer<String, TradingInfoDTO> consumer = new KafkaConsumer<>(
                Map.of(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers(),
                    ConsumerConfig.GROUP_ID_CONFIG, "candle_consumers",
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                ),
                new StringDeserializer(),
                jsonDeserializer
            );
        ) {
            consumer.subscribe(Collections.singletonList(KafkaConfig.CANDLES_TOPIC));

            final var message = genRandomTradingInfoDTO("rsi");

            producer.send(new ProducerRecord<>(KafkaConfig.CANDLES_TOPIC, "testcontainers", message));

            Unreliables.retryUntilTrue(
                1, TimeUnit.SECONDS,
                () -> {
                    ConsumerRecords<String, TradingInfoDTO> records = consumer.poll(Duration.ofMillis(100));
                    if (records.isEmpty()) {
                        return false;
                    }
                    assertThat(records)
                            .hasSize(1)
                            .extracting(ConsumerRecord::topic, ConsumerRecord::key, ConsumerRecord::value)
                            .containsExactly(tuple(KafkaConfig.CANDLES_TOPIC, "testcontainers", message));
                    return true;
                }
            );

            consumer.unsubscribe();
        }
    }
}
