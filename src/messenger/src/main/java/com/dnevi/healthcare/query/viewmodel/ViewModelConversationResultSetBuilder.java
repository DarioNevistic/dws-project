package com.dnevi.healthcare.query.viewmodel;

import com.dnevi.healthcare.domain.model.conversation.Conversation;

public class ViewModelConversationResultSetBuilder {

    private ViewModelConversationResultSetBuilder() {
    }

    public static ViewModelConversation buildSingle(Conversation conversation) {
        var conversationViewModel = new ViewModelConversation();
        conversationViewModel.setId(conversation.getId());
        conversationViewModel.setTitle(conversation.getTitle());
        conversationViewModel.setCreator(conversation.getCreator().getEmail());
        conversationViewModel.setDeleted(conversation.isDeleted());

        return conversationViewModel;
    }
}
