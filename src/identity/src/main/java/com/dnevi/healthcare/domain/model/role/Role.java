package com.dnevi.healthcare.domain.model.role;

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

/**
 * The type Role. Defines the role and the list of users who are associated with that role
 */
@Entity
@Table(name = "role")
@NoArgsConstructor
@Getter
@EntityListeners(PersistenceTimestampableListener.class)
public class Role implements PersistenceTimestampable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true, columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    private RoleName role;

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Setter
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    @Version
    @Column(nullable = false)
    protected Long version;

    public Role(RoleName role) {
        this.role = role;
    }

    public boolean isAdminRole() {
        return this.role.equals(RoleName.ROLE_ADMIN);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getRole() {
        return role;
    }

    public void setRole(RoleName role) {
        this.role = role;
    }
}
