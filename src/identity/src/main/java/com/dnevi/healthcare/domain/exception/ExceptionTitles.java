package com.dnevi.healthcare.domain.exception;

import com.nsoft.chiwava.core.error.Title;

public enum ExceptionTitles implements Title {

    INVALID_INVITATION_TOKEN("exception.invalid_invitation_token"),
    INVALID_JWT_TOKEN("exception.invalid_jwt_token"),
    USER_LOGIN_FAILED("exception.user_login_failed"),
    MISSING_INVITATION_TOKEN("exception.missing_invitation_token"),
    INVALID_USER_TYPE("exception.invalid_user_type"),
    NOT_A_VALID_EMAIL("exception.not_a_valid_email"),
    BAD_CREDENTIALS("exception.bad_credentials"),
    USER_NOT_FOUND("exception.user_not_found"),
    RESOURCE_ALREADY_IN_USE("exception.resource_already_in_use"),
    INVITATION_EXPIRED("exception.invitation_expired"),
    INVITATION_NOT_FOUND("exception.invitation_not_found"),
    INVITATION_TOKEN_NOT_FOUND("exception.invitation_token_not_found"),
    EMAIL_COULD_NOT_BE_SENT("exception.email_could_not_be_sent");

    private final String title;

    ExceptionTitles(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
