package com.dnevi.healthcare.domain.model.invitation;

import com.dnevi.healthcare.domain.exception.InvitationExpiredException;
import com.dnevi.healthcare.domain.exception.NotValidEmailException;
import com.dnevi.healthcare.util.StringUtil;
import com.nsoft.chiwava.core.persistence.PersistenceTimestampable;
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
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;

@Entity
@Table(name = "invitation")
@Getter
@NoArgsConstructor
@EntityListeners(PersistenceTimestampableListener.class)
public class Invitation implements PersistenceTimestampable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @Column(name = "receiver", nullable = false, updatable = false, columnDefinition = "VARCHAR(254)")
    private String receiver;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(20)")
    private InvitationStatus status;

    @Column(name = "expiration", updatable = false)
    private LocalDateTime expiration;

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Setter
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    @Version
    @Column(nullable = false)
    protected Long version;


    public Invitation(String receiver) {
        this(receiver, 2_628_000L);
    }


    public Invitation(String receiver, Long invitationValidity) {
        if (!StringUtil.isEmail(receiver)) {
            throw new NotValidEmailException(receiver);
        }

        this.receiver = receiver;
        this.status = InvitationStatus.CREATED;
        this.expiration = invitationValidity == null ? null
                : LocalDateTime.now().plusSeconds(invitationValidity);
    }

    public InvitationToken createToken() {
        if (isExpired()) {
            throw new InvitationExpiredException();
        }

        return new InvitationToken(this);
    }

    public boolean isExpired() {
        if (expiration == null) {
            return false;
        }

        return expiration.isBefore(LocalDateTime.now());
    }
}
