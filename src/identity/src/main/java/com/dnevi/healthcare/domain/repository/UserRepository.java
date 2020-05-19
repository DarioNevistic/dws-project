package com.dnevi.healthcare.domain.repository;

import com.dnevi.healthcare.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT e FROM #{#entityName} e "
            + "WHERE (CONCAT(:emails) is null or e.id IN (:emails))")
    Optional<List<User>> fetchByEmails(@Nullable @Param("emails") List<String> emails);
}
