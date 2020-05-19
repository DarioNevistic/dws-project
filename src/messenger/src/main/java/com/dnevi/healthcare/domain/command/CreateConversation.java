package com.dnevi.healthcare.domain.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateConversation {
    private String title;
    private String creator;
    private String participant;
}
