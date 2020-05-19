package com.dnevi.healthcare.domain.model.invitation;

import com.nsoft.chiwava.core.persistence.PersistenceTimestampable;
import com.nsoft.chiwava.core.persistence.listener.PersistenceTimestampableListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "invitation_token")
@Getter
@NoArgsConstructor
@EntityListeners(PersistenceTimestampableListener.class)
public class InvitationToken implements PersistenceTimestampable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @OneToOne
    @JoinColumn(name = "invitation_id", nullable = false, updatable = false)
    private Invitation invitation;

    @Column(name = "hash", nullable = false, updatable = false, columnDefinition = "VARCHAR(40)")
    private String hash;

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Setter
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    @Version
    @Column(nullable = false)
    protected Long version;

    public InvitationToken(Invitation invitation) {
        this.invitation = Objects.requireNonNull(invitation);
        this.hash = DigestUtils.sha1Hex(UUID.randomUUID().toString().replace("-", ""));
    }

    public String getEncodedToken() {
        return DigestUtils.sha1Hex(
                String.format("%s_%s", invitation.getReceiver(), hash));
    }

    public boolean isExpired() {
        return invitation.isExpired();
    }

}
