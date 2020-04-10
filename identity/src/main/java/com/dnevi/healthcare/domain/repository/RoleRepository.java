package com.dnevi.healthcare.domain.repository;

import com.dnevi.healthcare.domain.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
