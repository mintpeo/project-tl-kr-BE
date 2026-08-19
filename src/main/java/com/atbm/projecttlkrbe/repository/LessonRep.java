package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRep extends JpaRepository<Lesson, Long> {
}
