package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.LessonRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRouteRep extends JpaRepository<LessonRoute, Long> {
    List<LessonRoute> findByCateRouteId(long cateRouteId);
}
