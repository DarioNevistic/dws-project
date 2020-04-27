package com.dnevi.healthcare.query.viewmodel;

import com.dnevi.healthcare.domain.model.conversation.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewModelMessage {
    private Long id;
    private String sender;
    private String recipient;
    private String message;
    private MessageType messageType;
    private Long conversationId;
}
