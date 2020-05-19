package com.dnevi.healthcare.query.viewmodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewModelConversation {
    private Long id;
    private String title;
    private String creator;
    private boolean deleted;
}
