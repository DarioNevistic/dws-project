package com.dnevi.healthcare.domain.repository;

import com.dnevi.healthcare.domain.model.user.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
