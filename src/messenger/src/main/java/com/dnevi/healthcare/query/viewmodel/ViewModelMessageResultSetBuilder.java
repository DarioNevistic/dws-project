package com.dnevi.healthcare.query.viewmodel;

import com.dnevi.healthcare.domain.model.conversation.InstantMessage;

public class ViewModelMessageResultSetBuilder {
    private ViewModelMessageResultSetBuilder() {
    }

    public static ViewModelMessage buildOne(InstantMessage instantMessage) {
        var viewModelMessage = new ViewModelMessage();
        viewModelMessage.setId(instantMessage.getId());
        viewModelMessage.setSender(instantMessage.getSender().getEmail());
        viewModelMessage.setMessage(instantMessage.getPayload());
        viewModelMessage.setConversationId(instantMessage.getConversation().getId());
        viewModelMessage.setMessageType(instantMessage.getMessageType());

        return viewModelMessage;
    }
}
