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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import ru.ct.belfort.kafka.producers.ErrorProducerConfig;
import ru.ct.belfort.kafka.producers.IdeasProducerConfig;
import ru.ct.belfort.strategy.RsiStrategy;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

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

    static KafkaProducer<String, TradingInfoDTO> producer = null;
    static KafkaConsumer<String, IdeaDTO> ideasConsumer = null;
    static KafkaConsumer<String, String> errorConsumer = null;

    @BeforeAll
    public static void init() {

        JsonDeserializer<IdeaDTO> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");

        producer = new KafkaProducer<>(
                Map.of(
                        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers(),
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                        ProducerConfig.CLIENT_ID_CONFIG, "candles_test_producer"
                ),
                new StringSerializer(),
                new JsonSerializer<>()
        );

        ideasConsumer = new KafkaConsumer<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers(),
                        ConsumerConfig.GROUP_ID_CONFIG, "ideas_test_consumers",
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                ),
                new StringDeserializer(),
                jsonDeserializer
        );

        errorConsumer = new KafkaConsumer<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers(),
                        ConsumerConfig.GROUP_ID_CONFIG, "error_test_consumers",
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                ),
                new StringDeserializer(),
                new StringDeserializer()
        );

        ideasConsumer.subscribe(Collections.singletonList(IdeasProducerConfig.TOPIC));
        errorConsumer.subscribe(Collections.singletonList(ErrorProducerConfig.TOPIC));
    }

    public <V> void expectMessage(KafkaConsumer<String, V> consumer, String topic, V message) {
        ConsumerRecords<String, V> records = consumer.poll(Duration.ofMillis(1000));
        assertThat(records)
                .isNotEmpty()
                .hasSize(1)
                .extracting(ConsumerRecord::topic, ConsumerRecord::value)
                .containsExactly(tuple(topic, message));
    }

    public <V> void expectNoMessage(KafkaConsumer<String, V> consumer) {
        ConsumerRecords<String, V> records = consumer.poll(Duration.ofMillis(1000));
        assertThat(records)
                .isEmpty();
    }

    @Test
    public void sendBadCandles_ExpectBuyIdea() {
        final var message = new TradingInfoDTO(
                closePricesToCandles(new double[]{500, 493, 491, 485, 483, 482, 480, 467, 463, 461}),
                "rsi"
        );
        producer.send(new ProducerRecord<>(CandlesConsumerConfig.TOPIC, message));
        double coeff = new RsiStrategy().predict(message.candles());
        expectMessage(ideasConsumer, IdeasProducerConfig.TOPIC, new IdeaDTO(coeff, "Recommended to buy"));
        expectNoMessage(errorConsumer);
    }

    @Test
    public void sendGoodCandles_ExpectSellIdea() {
        final var message = new TradingInfoDTO(
                closePricesToCandles(new double[]{500, 505, 517, 523, 524, 525, 536, 541, 547, 555}),
                "rsi"
        );
        producer.send(new ProducerRecord<>(CandlesConsumerConfig.TOPIC, message));
        double coeff = new RsiStrategy().predict(message.candles());
        expectMessage(ideasConsumer, IdeasProducerConfig.TOPIC, new IdeaDTO(coeff, "Recommended to sell"));
        expectNoMessage(errorConsumer);
    }

    @Test
    public void sendOkCandles_ExpectNoIdea() {
        final var message = new TradingInfoDTO(
                closePricesToCandles(new double[]{500, 500, 500, 500, 500, 500, 500, 500, 500, 500}),
                "rsi"
        );
        producer.send(new ProducerRecord<>(CandlesConsumerConfig.TOPIC, message));
        expectNoMessage(ideasConsumer);
        expectNoMessage(errorConsumer);
    }

    @Test
    public void sendUnknownStrategy_ExpectError() {
        final var message = new TradingInfoDTO(
                closePricesToCandles(new double[]{}),
                "fds7f757das78f7sd"
        );
        producer.send(new ProducerRecord<>(CandlesConsumerConfig.TOPIC, message));
        expectMessage(errorConsumer, ErrorProducerConfig.TOPIC, "Unknown strategy");
        expectNoMessage(ideasConsumer);
    }

    @AfterAll
    public static void destructor() {
        ideasConsumer.unsubscribe();
    }
}
