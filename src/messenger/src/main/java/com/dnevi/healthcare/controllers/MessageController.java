package com.dnevi.healthcare.controllers;

import com.dnevi.healthcare.application.MessagingService;
import com.dnevi.healthcare.domain.command.SendInstantMessage;
import com.dnevi.healthcare.domain.model.user.User;
import com.dnevi.healthcare.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class MessageController {
    private final MessagingService messagingService;

    @Autowired
    public MessageController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @MessageMapping("/im")
    public void instantMessage(@Valid SendInstantMessage message, @CurrentUser User currentUser) {
        message.setFrom(currentUser);
        this.messagingService.sendMessage(message);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
