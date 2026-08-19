package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.LessonCategoryRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonCategoryRouteRep extends JpaRepository<LessonCategoryRoute, Long> {
}
