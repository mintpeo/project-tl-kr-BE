package com.atbm.projecttlkrbe.repository;


import com.atbm.projecttlkrbe.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRep extends JpaRepository<Auth, Long> {
    Optional<Auth> findByEmail(String email);
    List<Auth> findByEmailContainingIgnoreCase(String email);
}
