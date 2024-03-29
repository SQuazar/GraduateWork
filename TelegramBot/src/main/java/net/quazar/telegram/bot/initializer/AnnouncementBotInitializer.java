package net.quazar.telegram.bot.initializer;

import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.AnnouncementsBot;
import net.quazar.telegram.bot.handler.CallbackDataHandler;
import net.quazar.telegram.bot.handler.StateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Collection;

@AllArgsConstructor
public class AnnouncementBotInitializer implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementBotInitializer.class);

    private final AnnouncementsBot announcementsBot;
    private final Collection<StateHandler> stateHandlers;
    private final Collection<CallbackDataHandler> callbackDataHandlers;

    @Override
    public void afterPropertiesSet() throws Exception {
        for (StateHandler stateHandler : stateHandlers) {
            StateHandler.Handler handlerAnnotation = AnnotationUtils.findAnnotation(stateHandler.getClass(), StateHandler.Handler.class);
            if (handlerAnnotation == null) {
                LOGGER.warn("Cannot register handler {} because this class isn't annotated as {}.",
                        stateHandler.getClass().getName(), StateHandler.Handler.class.getName());
                continue;
            }
            announcementsBot.getStateHandlerRegistry().register(handlerAnnotation.state(), stateHandler);
        }

        for (CallbackDataHandler callbackDataHandler : callbackDataHandlers) {
            CallbackDataHandler.Handler handlerAnnotation = AnnotationUtils.findAnnotation(callbackDataHandler.getClass(), CallbackDataHandler.Handler.class);
            if (handlerAnnotation == null) {
                LOGGER.warn("Cannot register callback data handler {} because this class isn't annotated as {}",
                        callbackDataHandler.getClass().getName(), CallbackDataHandler.Handler.class.getName());
                continue;
            }
            for (String callbackData : handlerAnnotation.callbackData())
                announcementsBot.getCallbackDataHandlerRegistry().register(callbackData, callbackDataHandler);
        }
    }
}
