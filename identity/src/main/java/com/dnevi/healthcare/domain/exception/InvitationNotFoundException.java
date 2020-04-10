package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class InvitationNotFoundException extends AbstractError {
    public InvitationNotFoundException(String receiver) {
        super(String.format("Invitation for email '%s' not found.", receiver));
    }

    @Override
    public Status getStatus() {
        return Status.NOT_FOUND;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.INVITATION_NOT_FOUND;
    }
}
