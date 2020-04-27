package com.dnevi.healthcare.domain.exception;

import com.dnevi.healthcare.domain.model.user.UserType;
import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class CannotCreateConversationException extends AbstractError {

    public CannotCreateConversationException(String message) {
        super(message);
    }

    public static CannotCreateConversationException participantsNotFound() {
        var message = "No participants found for provided emails.";
        return new CannotCreateConversationException(message);
    }

    public static CannotCreateConversationException notAuthorized(UserType userType) {
        var message = String
                .format("User with the user type '%s' cannot create conversation. Only EMPLOYEES can create conversations.",
                        userType);
        return new CannotCreateConversationException(message);
    }

    @Override
    public Status getStatus() {
        return Status.BAD_REQUEST;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.CANNOT_CREATE_CONVERSATION;
    }
}
