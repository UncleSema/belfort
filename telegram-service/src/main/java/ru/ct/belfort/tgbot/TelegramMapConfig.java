package ru.ct.belfort.tgbot;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.HashMap;


@Configuration
public class TelegramMapConfig {
    @Bean
    public HashMap<Long, ParsingMode> mapProducerFactory() {
        return new HashMap<>();
    }

    @Bean
    public BotMapHandler botMapHandlerProducerFactory(@Qualifier("mapProducerFactory") HashMap<Long, ParsingMode> MODE_MAP) {
        return new BotMapHandler(MODE_MAP);
    }
}
