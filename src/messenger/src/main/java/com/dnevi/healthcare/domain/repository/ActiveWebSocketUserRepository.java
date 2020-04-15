package com.dnevi.healthcare.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActiveWebSocketUserRepository extends JpaRepository<Object, Long> {

    // TODO rewrite this
    List<String> findAllActiveUsers();
}
