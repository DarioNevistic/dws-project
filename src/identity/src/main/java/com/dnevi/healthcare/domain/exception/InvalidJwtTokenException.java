package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class InvalidJwtTokenException extends AbstractError {
    public InvalidJwtTokenException() {
        super("Expired or invalid JWT token.");
    }

    @Override
    public Status getStatus() {
        return Status.BAD_REQUEST;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.INVALID_JWT_TOKEN;
    }
}
