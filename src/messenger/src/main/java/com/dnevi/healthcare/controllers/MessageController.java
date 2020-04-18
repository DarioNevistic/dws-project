package com.dnevi.healthcare.controllers;

import com.dnevi.healthcare.domain.InstantMessage;
import com.dnevi.healthcare.domain.model.user.User;
import com.dnevi.healthcare.domain.repository.ActiveWebSocketUserRepository;
import com.dnevi.healthcare.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ActiveWebSocketUserRepository activeUserRepository;

    @Autowired
    public MessageController(
            SimpMessageSendingOperations messagingTemplate,
            ActiveWebSocketUserRepository activeUserRepository) {
        this.messagingTemplate = messagingTemplate;
        this.activeUserRepository = activeUserRepository;
    }

    @MessageMapping("/im")
    public void im(InstantMessage im, @CurrentUser User currentUser) {
        im.setFrom(currentUser.getEmail());
        this.messagingTemplate.convertAndSendToUser(im.getTo(), "/queue/messages", im);
        this.messagingTemplate.convertAndSendToUser(im.getFrom(), "/queue/messages", im);
    }

//    @SubscribeMapping("/users")
//    public List<String> subscribeMessages() {
////        return this.activeUserRepository.findAllActiveUsers();
//    }
}
