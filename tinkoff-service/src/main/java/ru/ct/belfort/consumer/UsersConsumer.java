package ru.ct.belfort.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ct.belfort.CandleSubscriptionService;
import ru.ct.belfort.OperationSubscriptionService;
import ru.ct.belfort.UserDTO;
import ru.ct.belfort.client.TinkoffClient;
import ru.ct.belfort.client.TinkoffClientService;
import ru.ct.belfort.producer.ErrorsProducer;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersConsumer {

    private final CandleSubscriptionService candleSubscriptionService;
    private final OperationSubscriptionService operationSubscriptionService;

    private final TinkoffClientService tinkoffClientService;

    private final ErrorsProducer errorsProducer;

    @KafkaListener(topics = "ct.belfort.telegram.users",
            groupId = "tinkoff_service_consumers",
            containerFactory = "UsersConsumerContainerFactory")
    public void consume(ConsumerRecord<String, UserDTO> record) {
        log.info("Message is delivered!");
        UserDTO dto = record.value();
        TinkoffClient client = tinkoffClientService.getClientByToken(dto.token());
        if (!client.isTokenValid()) {
            log.error("Invalid token!");
            errorsProducer.sendMessage("Token=" + dto.token() + " -- is invalid");
        } else if (!client.isListOfFigisValid(dto.figis())) {
            log.error("Invalid list of figis!");
            errorsProducer.sendMessage("ListOfFigis=" + dto.figis() + " -- is invalid");
        } else {
            candleSubscriptionService.subscribe(client, dto.figis());
            operationSubscriptionService.subscribe(client);
        }
    }
}