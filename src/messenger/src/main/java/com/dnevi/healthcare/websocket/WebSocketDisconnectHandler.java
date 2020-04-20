package com.dnevi.healthcare.websocket;


import com.dnevi.healthcare.domain.repository.ActiveWebSocketUserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class WebSocketDisconnectHandler<S> implements ApplicationListener<SessionDisconnectEvent> {
    private ActiveWebSocketUserRepository activeWsUserRepository;
    private SimpMessageSendingOperations messagingTemplate;

    public WebSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate,
            ActiveWebSocketUserRepository activeWsUserRepository) {
        super();
        this.messagingTemplate = messagingTemplate;
        this.activeWsUserRepository = activeWsUserRepository;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        String id = event.getSessionId();

        this.activeWsUserRepository.findById(id).ifPresent((user) -> {
            this.activeWsUserRepository.deleteById(id);
//            this.messagingTemplate.convertAndSend("/topic/friends/signout",
//                    Collections.singletonList(user.getUsername()));
        });
    }

}