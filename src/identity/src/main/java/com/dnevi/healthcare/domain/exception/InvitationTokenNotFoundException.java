package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class InvitationTokenNotFoundException extends AbstractError {
    public InvitationTokenNotFoundException(String receiver) {
        super(String.format("Invitation token for the receiver '%s' not found.", receiver));
    }

    @Override
    public Status getStatus() {
        return Status.NOT_FOUND;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.INVITATION_TOKEN_NOT_FOUND;
    }
}
