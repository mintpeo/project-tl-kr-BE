package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.UserStreak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStreakRep extends JpaRepository<UserStreak, Long> {
    UserStreak findByUser_Id(Long userId);
}
