package com.dnevi.healthcare.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class InstantMessage {
    private String from;
    @NotNull(message = "Specify message receiver")
    private String to;
    @NotNull(message = "Cannot send empty message")
    private String message;
}
