package ru.ct.belfort.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ct.belfort.Advice;
import ru.ct.belfort.IdeaDTO;
import ru.ct.belfort.base.KafkaAndPostgresTestBase;
import ru.ct.belfort.kafka.consumers.CandlesConsumerConfig;
import ru.ct.belfort.kafka.producers.ErrorProducerConfig;
import ru.ct.belfort.kafka.producers.IdeasProducerConfig;
import ru.ct.belfort.strategy.RsiStrategy;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static ru.ct.belfort.Utils.*;

@Testcontainers
@SpringBootTest
public class KafkaTest extends KafkaAndPostgresTestBase {

    private static final long MAX_BLOCK_TIME = 1000;

    public static <V> void expectMessage(KafkaConsumer<String, V> consumer, String topic, V message) {
        ConsumerRecords<String, V> records = consumer.poll(Duration.ofMillis(MAX_BLOCK_TIME));
        assertThat(records)
                .isNotEmpty()
                .hasSize(1)
                .extracting(ConsumerRecord::topic, ConsumerRecord::value)
                .containsExactly(tuple(topic, message));
    }

    public static <V> void expectNoMessage(KafkaConsumer<String, V> consumer) {
        ConsumerRecords<String, V> records = consumer.poll(Duration.ofMillis(MAX_BLOCK_TIME));
        assertThat(records)
                .isEmpty();
    }

    @Test
    public void sendBadCandles_ExpectBuyIdea() {
        final var message = badCandlesSample();
        producer.send(new ProducerRecord<>(CandlesConsumerConfig.TOPIC, message));
        double score = new RsiStrategy().predict(message.candles());
        expectMessage(ideasConsumer, IdeasProducerConfig.TOPIC, new IdeaDTO(score, Advice.BUY));
        expectNoMessage(errorConsumer);
    }

    @Test
    public void sendGoodCandles_ExpectSellIdea() {
        final var message = goodCandlesSample();
        producer.send(new ProducerRecord<>(CandlesConsumerConfig.TOPIC, message));
        double score = new RsiStrategy().predict(message.candles());
        expectMessage(ideasConsumer, IdeasProducerConfig.TOPIC, new IdeaDTO(score, Advice.SELL));
        expectNoMessage(errorConsumer);
    }

    @Test
    public void sendOkCandles_ExpectNoIdea() {
        producer.send(new ProducerRecord<>(CandlesConsumerConfig.TOPIC, okCandlesSample()));
        expectNoMessage(ideasConsumer);
        expectNoMessage(errorConsumer);
    }

    @Test
    public void sendUnknownStrategy_ExpectError() {
        producer.send(new ProducerRecord<>(CandlesConsumerConfig.TOPIC, unknownStrategySample()));
        expectMessage(errorConsumer, ErrorProducerConfig.TOPIC, "Unknown strategy");
        expectNoMessage(ideasConsumer);
    }
}
