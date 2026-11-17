package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.LevelExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelExamRep extends JpaRepository<LevelExam, Long> {
    Optional<LevelExam> findFirstByUser_IdOrderByPercentDescIdDesc(Long userId);
    List<LevelExam> findByUser_Id(Long userId);
}
