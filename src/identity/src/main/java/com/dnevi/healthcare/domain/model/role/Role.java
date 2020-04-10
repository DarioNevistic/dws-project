package com.dnevi.healthcare.domain.model.role;

import com.dnevi.healthcare.domain.model.user.User;
import com.nsoft.chiwava.core.persistence.PersistenceTimestampable;
import com.nsoft.chiwava.core.persistence.listener.PersistenceTimestampableListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Role. Defines the role and the list of users who are associated with that role
 */
@Entity
@Table(name = "role")
@NoArgsConstructor
@Getter
@ToString
@EntityListeners(PersistenceTimestampableListener.class)
public class Role implements PersistenceTimestampable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true, columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    @NaturalId
    private RoleName role;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

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

    public Set<User> getUserList() {
        return users;
    }

    public void setUserList(Set<User> userList) {
        this.users = userList;
    }
}
