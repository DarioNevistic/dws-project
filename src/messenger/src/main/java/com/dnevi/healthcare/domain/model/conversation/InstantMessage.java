package com.dnevi.healthcare.domain.model.conversation;

import com.dnevi.healthcare.domain.model.user.User;
import com.nsoft.chiwava.core.persistence.PersistenceCreateTimestampable;
import com.nsoft.chiwava.core.persistence.listener.PersistenceCreateTimestampableListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;

@Entity
@Table(name = "instant_message")
@Getter
@NoArgsConstructor
@EntityListeners(PersistenceCreateTimestampableListener.class)
@ParametersAreNonnullByDefault
public class InstantMessage implements PersistenceCreateTimestampable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private User sender;

    @Column(name = "payload", nullable = false, updatable = false, columnDefinition = "text")
    private String payload;

    @Column(name = "message_type", columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Version
    @Column(nullable = false)
    protected Long version;

    public InstantMessage(User sender, String payload, MessageType messageType) {
        this.sender = sender;
        this.payload = payload;
        this.messageType = messageType;
    }
}
