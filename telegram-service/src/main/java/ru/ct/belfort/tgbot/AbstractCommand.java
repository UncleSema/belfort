package ru.ct.belfort.tgbot;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

abstract class AbstractCommand extends BotCommand {

    private final BotMapHandler mapHandler;

    AbstractCommand(String identifier, String description, BotMapHandler handler) {
        super(identifier, description);
        this.mapHandler = handler;
    }

    protected void setMode(Long chatID, ParsingMode mode) {
        mapHandler.setMode(chatID, mode);
    }

    void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, String text) {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(chatId.toString());

        message.setText(text);
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}