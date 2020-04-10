package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class AuthenticationFailed extends AbstractError {

    public AuthenticationFailed(String email) {
        super(String.format("Authentication failed for a user with the email '%s'", email));
    }

    @Override
    public Status getStatus() {
        return Status.BAD_REQUEST;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.BAD_CREDENTIALS;
    }
}
