package com.dnevi.healthcare.domain.repository;

import com.dnevi.healthcare.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveWebSocketUserRepository extends JpaRepository<User, Long> {

}
