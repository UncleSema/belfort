package ru.ct.belfort.tgbot;


import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;


public class FigisCommand extends AbstractCommand {


    public FigisCommand(String identifier, String description, BotMapHandler handler) {
        super(identifier, description, handler);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        //формируем имя пользователя - поскольку userName может быть не заполнено, для этого случая используем имя и фамилию пользователя
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());

        StringBuilder msg = new StringBuilder("Choose figis");
        setMode(chat.getId(), ParsingMode.FIGIS);
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                msg.toString());
    }


}