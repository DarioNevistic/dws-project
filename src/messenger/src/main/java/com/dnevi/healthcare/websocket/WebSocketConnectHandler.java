package com.dnevi.healthcare.websocket;


import com.dnevi.healthcare.domain.model.ActiveWebSocketUser;
import com.dnevi.healthcare.domain.repository.ActiveWebSocketUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;

@Slf4j
public class WebSocketConnectHandler implements ApplicationListener<SessionConnectEvent> {

    private ActiveWebSocketUserRepository activeWsUserRepository;

    private SimpMessageSendingOperations messagingTemplate;

    public WebSocketConnectHandler(SimpMessageSendingOperations messagingTemplate,
            ActiveWebSocketUserRepository activeWsUserRepository) {
        super();
        this.messagingTemplate = messagingTemplate;
        this.activeWsUserRepository = activeWsUserRepository;
    }

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        MessageHeaders headers = event.getMessage().getHeaders();
        Principal user = SimpMessageHeaderAccessor.getUser(headers);
        if (user == null) {
            return;
        }

        String id = SimpMessageHeaderAccessor.getSessionId(headers);
        this.activeWsUserRepository.save(new ActiveWebSocketUser(id, user.getName()));
    }
}
