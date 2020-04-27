package com.dnevi.healthcare.query.viewmodel;

import com.dnevi.healthcare.domain.model.conversation.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ViewModelMessage {
    private Long id;
    private String sender;
    private String message;
    private MessageType messageType;
    private Long conversationId;
    private LocalDateTime createdAt;
}
