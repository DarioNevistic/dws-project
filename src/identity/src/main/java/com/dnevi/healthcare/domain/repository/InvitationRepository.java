package com.dnevi.healthcare.domain.repository;

import com.dnevi.healthcare.domain.model.invitation.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findOneByReceiver(String receiver);
}
