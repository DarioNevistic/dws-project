package com.dnevi.healthcare.domain.model.conversation;

import com.dnevi.healthcare.domain.model.user.User;
import com.nsoft.chiwava.core.persistence.PersistenceCreateTimestampable;
import com.nsoft.chiwava.core.persistence.listener.PersistenceTimestampableListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@NoArgsConstructor
@EntityListeners(PersistenceTimestampableListener.class)
@Getter
public class Message implements PersistenceCreateTimestampable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_user_id")
    private User recipient;

    @Column(name = "payload", nullable = false, updatable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "message_type", columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Version
    @Column(nullable = false)
    protected Long version;

    public Message(User sender, User recipient, String payload,
            MessageType messageType,
            Conversation conversation) {
        this.sender = sender;
        this.recipient = recipient;
        this.payload = payload;
        this.messageType = messageType;
        this.conversation = conversation;
    }
}
