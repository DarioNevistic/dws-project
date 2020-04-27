package com.dnevi.healthcare.domain.repository;

import com.dnevi.healthcare.domain.model.conversation.InstantMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstantMessageRepository extends JpaRepository<InstantMessage, Long> {
}
