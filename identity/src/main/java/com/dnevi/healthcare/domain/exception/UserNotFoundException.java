package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class UserNotFoundException extends AbstractError {

    public UserNotFoundException(String receiver) {
        super(String.format("User with the email '%s' not found.", receiver));
    }

    @Override
    public Status getStatus() {
        return Status.NOT_FOUND;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.USER_NOT_FOUND;
    }
}