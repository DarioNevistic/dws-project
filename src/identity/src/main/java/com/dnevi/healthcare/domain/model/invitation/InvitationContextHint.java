package com.dnevi.healthcare.domain.model.invitation;

import com.dnevi.healthcare.util.JsonMapperWrapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;

@Getter
public class InvitationContextHint {
    private String receiver;
    private LocalDateTime tokenExpiration;

    @JsonCreator
    public InvitationContextHint(
            @JsonProperty("receiver") String receiver,
            @JsonProperty("tokenExpiration") LocalDateTime tokenExpiration) {
        this.receiver = Objects.requireNonNull(receiver);
        this.tokenExpiration = Objects.requireNonNull(tokenExpiration);
    }

    @JsonIgnore
    public String toBase64() {
        return Base64.getEncoder().encodeToString(JsonMapperWrapper.toJson(this).getBytes());
    }

    @JsonIgnore
    public static InvitationContextHint fromBase64(String hint) {
        String decodedHint = new String(Base64.getDecoder().decode(hint.getBytes()));
        return JsonMapperWrapper.fromJson(decodedHint, InvitationContextHint.class);
    }
}
