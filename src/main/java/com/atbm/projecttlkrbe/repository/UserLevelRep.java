package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.UserLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLevelRep extends JpaRepository<UserLevel, Long> {
    Optional<UserLevel> findByUserId(Long userId);
}