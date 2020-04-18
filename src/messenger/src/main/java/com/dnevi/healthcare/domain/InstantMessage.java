package com.dnevi.healthcare.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstantMessage {
    private String to;
    private String from;
    private String message;
}
