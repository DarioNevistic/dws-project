package com.dnevi.healthcare.domain.model.user.patient;

import com.dnevi.healthcare.domain.model.user.User;
import com.nsoft.chiwava.core.persistence.PersistenceTimestampable;
import com.nsoft.chiwava.core.persistence.listener.PersistenceTimestampableListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@ToString
@Getter
@EntityListeners(PersistenceTimestampableListener.class)
@Table(name = "patient")
public class Patient implements PersistenceTimestampable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Setter
    @Column(name = "blood_type", columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Setter
    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Setter
    @Column(name = "country_of_birth", columnDefinition = "VARCHAR(50)")
    private String countryOfBirth;

    @Setter
    @Column(name = "place_of_birth", columnDefinition = "VARCHAR(50)")
    private String placeOfBirth;

    @Setter
    @Column(name = "gender", columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Setter
    @Column(name = "phone_number", columnDefinition = "VARCHAR(30)")
    private String phoneNumber;

    @Setter
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Setter
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    @Version
    @Column(nullable = false)
    protected Long version;

    public Patient(User user) {
        this.user = user;
    }

    public Patient(User user, BloodType bloodType, LocalDateTime dateOfBirth,
            String countryOfBirth, String placeOfBirth,
            Gender gender, String phoneNumber) {
        this.user = user;
        this.bloodType = bloodType;
        this.dateOfBirth = dateOfBirth;
        this.countryOfBirth = countryOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }
}
