package com.dnevi.healthcare.domain.command;

import com.dnevi.healthcare.domain.model.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel(value = "Registration Request", description = "The registration request payload")
@Getter
@Setter
public class RegistrationRequest {

    @ApiModelProperty(value = "A valid first name", allowableValues = "NonEmpty String")
    private String firstName;

    @ApiModelProperty(value = "A valid last name", allowableValues = "NonEmpty String")
    private String lastName;

    @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
    private String email;

    @NotNull(message = "Registration password cannot be null")
    @ApiModelProperty(value = "A valid password string", required = true, allowableValues = "NonEmpty String")
    private String password;

    @NotNull(message = "Specify whether the user has to be registered as an admin or not")
    @ApiModelProperty(value = "Flag denoting whether the user is an admin or not", required = true,
            dataType = "boolean", allowableValues = "true, false")
    private Boolean registerAsAdmin;

    @NotNull(message = "Specify user type")
    @ApiModelProperty(value = "A valid user type: PATIENT, DOCTOR or NURSE", required = true, allowableValues = "NonEmpty String")
    private UserType userType;
}
