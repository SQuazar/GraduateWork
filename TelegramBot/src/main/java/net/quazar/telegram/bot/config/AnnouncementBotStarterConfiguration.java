package net.quazar.telegram.bot.config;

import net.quazar.telegram.bot.AnnouncementsBot;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.bot.initializer.AnnouncementBotInitializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "announcementbot", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AnnouncementBotStarterConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AnnouncementBotInitializer botInitializer(AnnouncementsBot announcementsBot, ObjectProvider<List<StateHandler>> stateHandlers) {
        return new AnnouncementBotInitializer(announcementsBot, stateHandlers.getIfAvailable(Collections::emptyList));
    }

}
