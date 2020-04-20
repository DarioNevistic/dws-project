package com.dnevi.healthcare.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "active_ws_user")
@Getter
@NoArgsConstructor
public class ActiveWebSocketUser {

    @Id
    @Column(name = "session_id", columnDefinition = "VARCHAR(100)")
    private String sessionId;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(50)")
    private String username;

    @Setter
    @Column(name = "connection_time", nullable = false, updatable = false)
    private LocalDateTime connectionTime;

    public ActiveWebSocketUser(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        this.setConnectionTime(LocalDateTime.now());
    }
}
