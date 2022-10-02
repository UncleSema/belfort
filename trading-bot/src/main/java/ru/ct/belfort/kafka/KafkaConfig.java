package ru.ct.belfort.kafka;

public class KafkaConfig {
    public static final String CANDLES_TOPIC = "ct.belfort.invest.candles";
    public static final String ERROR_TOPIC = "ct.belfort.trade.error";
    public static final String IDEAS_TOPIC = "ct.belfort.trade.ideas";

    public static final String KAFKA_BOOTSTRAP_ADDRESS = "localhost:29092";
}
