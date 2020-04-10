package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class InvitationExpiredException extends AbstractError {

    public InvitationExpiredException() {
        super("Can't create token for an expired invitation");
    }

    @Override
    public Status getStatus() {
        return Status.BAD_REQUEST;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.INVITATION_EXPIRED;
    }
}
