package com.dnevi.healthcare.domain.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NotNull
public class ConfirmInvitation {
    @JsonIgnore
    private String invitationToken;

    @JsonIgnore
    private String hint;
}
