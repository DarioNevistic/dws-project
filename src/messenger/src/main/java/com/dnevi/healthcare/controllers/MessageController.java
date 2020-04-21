package com.dnevi.healthcare.controllers;

import com.dnevi.healthcare.application.MessagingService;
import com.dnevi.healthcare.domain.InstantMessage;
import com.dnevi.healthcare.domain.model.user.User;
import com.dnevi.healthcare.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class MessageController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final MessagingService messagingService;

    @Autowired
    public MessageController(
            SimpMessageSendingOperations messagingTemplate,
            MessagingService messagingService) {
        this.messagingTemplate = messagingTemplate;
        this.messagingService = messagingService;
    }

    @MessageMapping("/im")
    public void instantMessage(@Valid InstantMessage message, @CurrentUser User currentUser) {
        message.setFrom(currentUser.getEmail());

        // if all checks in messaging service OK ->
        this.messagingTemplate
                .convertAndSendToUser(message.getTo(), "/queue/private/messages", message);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
