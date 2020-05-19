package com.dnevi.healthcare.domain.exception;

import com.dnevi.healthcare.domain.model.user.UserType;
import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class InvalidUserTypeException extends AbstractError {
    public InvalidUserTypeException(UserType userType) {
        super(String.format("Invalid user type '%s'.", userType));
    }

    @Override
    public Status getStatus() {
        return Status.BAD_REQUEST;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.INVALID_USER_TYPE;
    }
}
