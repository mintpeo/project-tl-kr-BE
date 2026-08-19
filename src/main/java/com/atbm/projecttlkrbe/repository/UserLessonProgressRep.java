package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.UserLessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLessonProgressRep extends JpaRepository<UserLessonProgress, Long> {
    Optional<UserLessonProgress> findByUser_IdAndLessonRoute_Id(Long userId, Long lessonId);
    Optional<UserLessonProgress> findByLessonRoute_Id(Long lessonRouteId);
}
