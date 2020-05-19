package com.dnevi.healthcare.domain.repository;

import com.dnevi.healthcare.domain.model.conversation.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM #{#entityName} c "
            + "JOIN c.creator u "
            + "WHERE c.id = :id "
            + "AND u.id = :user_id")
    Optional<Conversation> findByIdAndUserId(@Param("id") Long id,
            @Param("user_id") Long userId);
}
