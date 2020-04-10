package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class InvitationEmailCouldNotBeSentException extends AbstractError {
    public InvitationEmailCouldNotBeSentException(String receiver) {
        super(String
                .format("Invitation for a user with the email '%s' could not be sent.", receiver));
    }

    @Override
    public Status getStatus() {
        return Status.BAD_REQUEST;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.EMAIL_COULD_NOT_BE_SENT;
    }
}
