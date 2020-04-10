package com.dnevi.healthcare.query.viewmodel;

import com.dnevi.healthcare.domain.model.user.UserType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class ViewModelUser {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean active;
    private Boolean emailVerified;
    private Set<String> roles;
    private UserType userType;
}
