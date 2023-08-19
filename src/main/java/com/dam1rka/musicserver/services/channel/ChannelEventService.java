package com.dam1rka.musicserver.services.channel;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChannelEventService extends SimpMessagingTemplate {

    public ChannelEventService(@Qualifier("brokerChannel") MessageChannel messageChannel) {
        super(messageChannel);
        setUserDestinationPrefix("/channel");
    }


    public void convertAndSendToChannel(String channel, String destination, Object payload) throws MessagingException {
        convertAndSendToUser(channel, destination, payload, (MessagePostProcessor) null);
    }
}
