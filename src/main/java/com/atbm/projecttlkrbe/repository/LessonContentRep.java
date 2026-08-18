package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.LessonContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonContentRep extends JpaRepository<LessonContent, Long> {
    Optional<LessonContent> findByLessonRoute_Id(Long lessonRouteId);
}
