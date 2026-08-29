package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.LessonCategoryRouteRes;
import com.atbm.projecttlkrbe.model.LessonRoute;
import com.atbm.projecttlkrbe.repository.LessonRouteRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminLessonSer {
    private final LessonRouteRep lessonRouteRep;
    private final LessonCategoryRouteSer lessonCategoryRouteSer;

        // Get Category Road
    public List<LessonCategoryRouteRes> getCateRoute() {
        return lessonCategoryRouteSer.getCateRoute();
    }

    // Search Lesson By Name
    public List<LessonRoute> getLessonByName(String lessonRoadName) {
        return lessonRouteRep.findByNameContainingIgnoreCase(lessonRoadName);
    }

    // Get Lesson Road
    public List<LessonRoute> getAllLessonRoutes() {
        return lessonRouteRep.findAll();
    }
}
