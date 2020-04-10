package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class InvalidInvitationTokenException extends AbstractError {
    public InvalidInvitationTokenException(String email) {
        super(String.format("Expired invitation token for an email '%s'", email));
    }

    @Override
    public Status getStatus() {
        return Status.BAD_REQUEST;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.INVALID_INVITATION_TOKEN;
    }
}
