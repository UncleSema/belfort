package ru.ct.belfort.tgbot;

import org.telegram.telegrambots.meta.bots.AbsSender;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;


public class StartCommand extends AbstractCommand {

    public StartCommand(String identifier, String description, BotMapHandler handler) {
        super(identifier, description, handler);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
//        формируем имя пользователя - поскольку userName может быть не заполнено, для этого случая используем имя и фамилию пользователя
//        String userName = (user.getUserName() != null) ? user.getUserName() :
//                String.format("%s %s", user.getLastName(), user.getFirstName());

        StringBuilder msg = new StringBuilder("List of available commands:");
        msg.append(System.lineSeparator()).append("/figis");
        msg.append(System.lineSeparator()).append("/strategy");
        msg.append(System.lineSeparator()).append("/token");
        msg.append(System.lineSeparator()).append("For now please enter your token, strategy and figis in that order separated by whitespace");

        setMode(chat.getId(), ParsingMode.INIT);
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                msg.toString());
    }
}