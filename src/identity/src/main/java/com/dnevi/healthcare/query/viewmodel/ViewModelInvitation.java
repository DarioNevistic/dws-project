package com.dnevi.healthcare.query.viewmodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewModelInvitation {
    private String receiver;
    private LocalDateTime expiration;
}
