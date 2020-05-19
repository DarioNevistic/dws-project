package com.dnevi.healthcare.domain.command;

import com.dnevi.healthcare.domain.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SendInstantMessage {
    @JsonIgnore
    private User from;

    @NotNull(message = "Specify message receiver")
    private String to;

    @NotNull(message = "Cannot send empty message")
    private String message;

    @NotNull(message = "Specify conversation ID")
    private Long conversationId;
}
