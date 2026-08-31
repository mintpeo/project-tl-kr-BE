package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.ResetPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResetPassRep extends JpaRepository<ResetPass, Long> {
    Optional<ResetPass> findFirstByAuthIdOrderByIdDesc(Long authId);
    List<ResetPass> findByAuth_Id(Long authId);
}
