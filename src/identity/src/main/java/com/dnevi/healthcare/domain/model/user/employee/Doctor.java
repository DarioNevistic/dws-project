package com.dnevi.healthcare.domain.model.user.employee;

import com.dnevi.healthcare.domain.model.user.User;
import com.dnevi.healthcare.domain.model.user.employee.EmployeeType.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Getter
@DiscriminatorValue(Constants.DOCTOR)
public class Doctor extends Employee {

    @Setter
    @Column(name = "specialization", columnDefinition = "VARCHAR(100)")
    private String specialization;

    public Doctor(User user) {
        super(user);
    }
}