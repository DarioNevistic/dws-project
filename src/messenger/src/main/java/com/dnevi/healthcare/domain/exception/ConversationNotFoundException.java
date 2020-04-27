package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class ConversationNotFoundException extends AbstractError {
    public ConversationNotFoundException(Long conversationId) {
        super(String.format("Conversation with the ID %s not found.", conversationId));
    }

    @Override
    public Status getStatus() {
        return Status.NOT_FOUND;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.CONVERSATION_NOT_FOUND;
    }
}
