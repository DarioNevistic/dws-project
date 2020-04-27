package com.dnevi.healthcare.domain.repository;

import com.dnevi.healthcare.domain.model.ActiveWebSocketUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActiveWebSocketUserRepository extends JpaRepository<ActiveWebSocketUser, String> {
    Optional<ActiveWebSocketUser> findByUsername(String username);
}
