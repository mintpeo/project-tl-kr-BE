package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.Verify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyRep extends JpaRepository<Verify, Long> {
    Optional<Verify> findFirstByAuthIdOrderByIdDesc(Long authId);
}
