package com.dnevi.healthcare.domain.repository;

import com.dnevi.healthcare.domain.model.conversation.InstantMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InstantMessageRepository extends JpaRepository<InstantMessage, Long> {

    @Query("SELECT e FROM #{#entityName} e "
            + "JOIN e.conversation c "
            + "WHERE c.id = :conversation_id")
    Page<InstantMessage> fetchMessagesByConversationId(
            @Param("conversation_id") Long conversationId, Pageable pageable);
}
