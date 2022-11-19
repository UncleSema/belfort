package ru.ct.belfort.db;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ct.belfort.TradingInfoDTO;
import ru.ct.belfort.base.KafkaAndPostgresTestBase;
import ru.ct.belfort.kafka.consumers.CandlesConsumerConfig;
import ru.ct.belfort.strategy.RsiStrategy;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.ct.belfort.Utils.*;

public class PostgresTest extends KafkaAndPostgresTestBase {

    @Autowired
    IdeasRepository repository;

    private static final long MAX_BLOCK_TIME = 1000;

    private static void sendAndWait(TradingInfoDTO message) {
        producer.send(new ProducerRecord<>(CandlesConsumerConfig.TOPIC, message));
        ideasConsumer.poll(Duration.ofMillis(MAX_BLOCK_TIME));
    }

    @Test
    public void sendBadCandles_ExpectDBInsertion() {
        final var message = badCandlesSample();
        var before = repository.getRecordsAmount();
        sendAndWait(message);
        assertEquals(before + 1, repository.getRecordsAmount());
        double score = new RsiStrategy().predict(message.candles());
        assertEquals(repository.getLastRecord().score(), score);
    }

    @Test
    public void sendGoodCandles_ExpectDBInsertion() {
        final var message = goodCandlesSample();
        var before = repository.getRecordsAmount();
        sendAndWait(message);
        assertEquals(before + 1, repository.getRecordsAmount());
        double score = new RsiStrategy().predict(message.candles());
        assertEquals(repository.getLastRecord().score(), score);
    }

    @Test
    public void sendOkCandles_ExpectNoDBInsertion() {
        var before = repository.getRecordsAmount();
        sendAndWait(okCandlesSample());
        assertEquals(before, repository.getRecordsAmount());
    }

    @Test
    public void sendUnknownStrategy_ExpectNoDBInsertion() {
        var before = repository.getRecordsAmount();
        sendAndWait(unknownStrategySample());
        assertEquals(before, repository.getRecordsAmount());
    }
}
