package ru.ct.belfort.tgbot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ct.belfort.UserDTO;
import ru.ct.belfort.kafka.producers.TestProducer;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingCommandBot {
    private final String BOT_NAME = "BelfortCT_bot";
    private final String BOT_TOKEN = System.getenv("BOT_TOKEN");

    private final TestProducer producer;

    private final BotMapHandler handler;

    @Autowired
    public TelegramBot(@Qualifier("botMapHandlerProducerFactory") BotMapHandler handler,
                       TestProducer producer) {
        super();
        this.handler = handler;
        this.producer = producer;
        registerAll(new StartCommand("start", "start", handler),
                new FigisCommand("figis", "figis", handler),
                new StrategyCommand("strategy", "strategy", handler),
                new TokenCommand("token", "Tinkoff Invest token", handler));
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    //Will get info from database, for now like that
    public UserDTO getInfo(Long chatID) {
        return new UserDTO(1,
                "token",
                "strat",
                List.of("1"));
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        if (!handler.hasModeSet(message.getChatId())) {
            handler.setMode(message.getChatId(), ParsingMode.COMMAND);
            sendMessage.setText("To begin trading, type /start");
        } else {
            ParsingMode mode = handler.getMode(message.getChatId());
            if (mode == ParsingMode.COMMAND) {
                sendMessage.setText("Unknown command!");
            } else {
                UserDTO current = getInfo(message.getChatId());
                UserDTO newInfo = null;
                switch (mode) {
                    case FIGIS -> {
                        newInfo = new UserDTO(current.id(), current.token(), current.strategy(),
                                List.of(message.getText()));
                        sendMessage.setText("Figis changed successfully");
                    }
                    case TOKEN -> {
                        newInfo = new UserDTO(current.id(), message.getText(), current.strategy(),
                                current.figis());
                        sendMessage.setText("Token changed successfully");
                    }
                    case STRATEGY -> {
                        newInfo = new UserDTO(current.id(), message.getText(), current.strategy(),
                                current.figis());
                        sendMessage.setText("Strategy changed successfully");
                    }
                    case INIT -> {
                        String[] data = message.getText().split("\\s+");
                        if (data.length < 3) {
                            sendMessage.setText("Please follow selected format, only " + data.length + " values were entered");
                            break;
                        }
                        newInfo = new UserDTO(current.id(), data[0], data[1], Arrays.asList(Arrays.copyOfRange(data, 2, data.length)));
                        sendMessage.setText("Default parameters set successfully");
                        handler.setMode(message.getChatId(), ParsingMode.COMMAND);
                    }

                }
                producer.sendMessage(newInfo);
            }
        }

        sendMessage.setChatId(message.getChatId());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Exception during registering bot", e);
        }
    }

    @Override
    public void processInvalidCommandUpdate(Update update) {
        super.processInvalidCommandUpdate(update);
    }

    @Override
    public boolean filter(Message message) {
        return super.filter(message);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

}
