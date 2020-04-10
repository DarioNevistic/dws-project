package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class ResourceAlreadyInUseException extends AbstractError {
    public ResourceAlreadyInUseException(String resourceName) {
        super(String.format("Email address '%s' already in use.", resourceName));
    }

    @Override
    public Status getStatus() {
        return Status.BAD_REQUEST;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.RESOURCE_ALREADY_IN_USE;
    }
}