package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.LessonCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonCategoryRep extends JpaRepository<LessonCategory, Long> {
}
