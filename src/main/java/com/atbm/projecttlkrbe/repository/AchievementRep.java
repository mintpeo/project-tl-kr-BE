package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRep extends JpaRepository<Achievement, Long> {
}
