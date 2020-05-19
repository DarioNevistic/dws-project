package com.dnevi.healthcare.security;

import com.dnevi.healthcare.domain.exception.InvalidJwtTokenException;
import com.dnevi.healthcare.domain.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private Long jwtExpirationInMs;

    @Value("${app.jwt.claims.refresh.name}")
    private String jwtClaimRefreshName;

    /**
     * Generates a token from a principal object. Embed the refresh token in the jwt so that a new
     * jwt can be created
     */
    public String generateToken(User userDetails) {
        Instant expiryDate = Instant.now().plusMillis(jwtExpirationInMs);
        var key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getId()))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret)
                .compact();
    }

    /**
     * Returns the user id encapsulated within the token
     */
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * Validates if a token has the correct un-malformed signature and is not expired or
     * unsupported.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(this.jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtTokenException();
        }
    }

    /**
     * Return the jwt expiration for the client so that they can execute the refresh token logic
     * appropriately
     */
    public Long getExpiryDuration() {
        return jwtExpirationInMs;
    }
}