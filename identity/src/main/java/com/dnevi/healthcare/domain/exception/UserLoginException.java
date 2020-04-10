package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class UserLoginException extends AbstractError {

    public UserLoginException(String email) {
        super(String.format("User with the email '%s' cannot be logged in.", email));
    }

    @Override
    public Status getStatus() {
        return Status.BAD_REQUEST;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.USER_LOGIN_FAILED;
    }
}