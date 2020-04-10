package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.AbstractError;
import com.nsoft.chiwava.core.error.Title;
import org.zalando.problem.Status;

public class NotValidEmailException extends AbstractError {
    public NotValidEmailException(String email) {
        super(String
                .format("The email '%s' is not a valid email.", email));
    }

    @Override
    public Status getStatus() {
        return Status.BAD_REQUEST;
    }

    @Override
    public Title getTitle() {
        return ExceptionTitles.NOT_A_VALID_EMAIL;
    }
}
