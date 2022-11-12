package ru.ct.belfort;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.ct.belfort.kafka.producers.TestProducer;
import ru.ct.belfort.tgbot.TelegramBot;

import java.util.List;


@RestController
public class RestApiController {


    private final TestProducer producer;

    public RestApiController(TestProducer producer, TelegramBot bot) {
        this.producer = producer;
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/telegram-service")
    public String service() {
        return "Telegram service is added!";
    }


    @GetMapping("/telegram-service-test")
    public String telegram() {
        producer.sendMessage(new UserDTO(1,
                "token",
                "strat",
                List.of("1")));

        return "Telegram service is working!";
    }

}
