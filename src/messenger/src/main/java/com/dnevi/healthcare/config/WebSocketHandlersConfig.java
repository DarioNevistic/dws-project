package com.dnevi.healthcare.config;

import com.dnevi.healthcare.domain.repository.ActiveWebSocketUserRepository;
import com.dnevi.healthcare.websocket.WebSocketConnectHandler;
import com.dnevi.healthcare.websocket.WebSocketDisconnectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.session.Session;

@Configuration
public class WebSocketHandlersConfig<S extends Session> {

    @Bean
    public WebSocketConnectHandler webSocketConnectHandler(
            SimpMessageSendingOperations messagingTemplate,
            ActiveWebSocketUserRepository repository) {
        return new WebSocketConnectHandler(messagingTemplate, repository);
    }

    @Bean
    public WebSocketDisconnectHandler<S> webSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate,
            ActiveWebSocketUserRepository repository) {
        return new WebSocketDisconnectHandler<>(messagingTemplate, repository);
    }

}