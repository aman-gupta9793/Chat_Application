package com.projects.ChatApplication.main.config;

import com.projects.ChatApplication.main.chat.ChatMessage;
import com.projects.ChatApplication.main.chat.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventLIstener
{
    private final SimpMessageSendingOperations messageTemplate;

    public WebSocketEventLIstener(SimpMessageSendingOperations messageTemplate)
    {
        this.messageTemplate = messageTemplate;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        //
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if(username != null){
            log.info("User disconnected: {}", username);
            var chatMessage = ChatMessage.builder().type(MessageType.LEAVE)
                    .sender(username).build();

            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
