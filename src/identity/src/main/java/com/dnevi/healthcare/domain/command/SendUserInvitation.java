package com.dnevi.healthcare.domain.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class SendUserInvitation {
    @NotNull
    private String receiver;

    @JsonCreator
    public SendUserInvitation(@JsonProperty("receiver") String receiver) {
        this.receiver = receiver;
    }
}
