package com.dnevi.healthcare.domain.repository;

import com.dnevi.healthcare.domain.model.user.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
