package ru.ct.belfort.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.Advice;
import ru.ct.belfort.IdeaDTO;
import ru.ct.belfort.client.TinkoffClientService;

@Slf4j
@RequiredArgsConstructor
@Service
public class IdeasConsumer {
    private final TinkoffClientService tinkoffClientService;

    @KafkaListener(topics = "ct.belfort.trade.ideas",
            groupId = "tinkoff_service_consumers",
            containerFactory = "IdeasConsumerContainerFactory")
    public void consume(ConsumerRecord<String, IdeaDTO> record) {
        Advice advice = record.value().advice();
        String strategy = record.value().strategy();
        String figi = record.value().figi();

        //TODO: get users with that (strategy, figi) from db and buy/sell for all of them
    }
}
