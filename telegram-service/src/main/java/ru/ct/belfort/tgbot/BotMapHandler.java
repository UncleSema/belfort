package ru.ct.belfort.tgbot;

import java.util.HashMap;

public class BotMapHandler {
    private final HashMap<Long, ParsingMode> MODE_MAP;


    public BotMapHandler(HashMap<Long, ParsingMode> MODE_MAP) {
        this.MODE_MAP = MODE_MAP;
    }

    public void setMode(Long chatID, ParsingMode mode) {
        MODE_MAP.put(chatID, mode);
    }

    public boolean hasModeSet(Long chatID) {
        return MODE_MAP.containsKey(chatID);
    }

    public ParsingMode getMode(Long chatID) {
        return MODE_MAP.get(chatID);
    }

}
