package com.dnevi.healthcare.query.viewmodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {

    private String accessToken;

    private String tokenType;

    private Long expiryDuration;

    public JwtAuthenticationResponse(String accessToken, Long expiryDuration) {
        this.accessToken = accessToken;
        this.expiryDuration = expiryDuration;
        tokenType = "Bearer ";
    }
}
