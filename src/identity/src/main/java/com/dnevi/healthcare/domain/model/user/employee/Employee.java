package com.dnevi.healthcare.domain.model.user.employee;

import com.dnevi.healthcare.domain.model.user.User;
import com.nsoft.chiwava.core.persistence.PersistenceTimestampable;
import com.nsoft.chiwava.core.persistence.listener.PersistenceTimestampableListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;


@Entity
@EntityListeners({PersistenceTimestampableListener.class})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Getter
@NoArgsConstructor
@ParametersAreNonnullByDefault
@Table(name = "employee")
public abstract class Employee implements PersistenceTimestampable {

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Setter
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    @Version
    @Column(nullable = false)
    protected Long version;

    public Employee(User user) {
        this.user = user;
    }
}
