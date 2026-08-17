package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.LessonCategoryRouteRes;
import com.atbm.projecttlkrbe.dto.response.LessonRouteRes;
import com.atbm.projecttlkrbe.model.LessonCategoryRoute;
import com.atbm.projecttlkrbe.model.LessonRoute;
import com.atbm.projecttlkrbe.model.UserLessonProgress;
import com.atbm.projecttlkrbe.repository.LessonCategoryRouteRep;
import com.atbm.projecttlkrbe.repository.LessonRouteRep;
import com.atbm.projecttlkrbe.repository.UserLessonProgressRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonCategoryRouteSer {
    private final LessonCategoryRouteRep rep;
    private final LessonRouteRep lessonRouteRep;
    private final UserLessonProgressRep userLessonProgressRep;

    // Get all category route
    public List<LessonCategoryRouteRes> getCateRoute() {
        List<LessonCategoryRoute> list = rep.findAll();
        List<LessonCategoryRouteRes> res = new ArrayList<>();
        for (LessonCategoryRoute l : list) {
            LessonCategoryRouteRes r = new LessonCategoryRouteRes();
            r.setId(l.getId());
            r.setName(l.getName());
            r.setOrderIndex(l.getOrderIndex());
            r.setDes(l.getDescription());
            // Lesson Size
            List<LessonRoute> lessons = lessonRouteRep.findByCateRouteId(l.getId());
            r.setLessonsSize(lessons.size());
            // Is Learn Category
            List<LessonRoute> lessonRoutes = lessonRouteRep.findByCateRouteId(l.getId());
            if (lessonRoutes.isEmpty()) r.setLearned(false);
            else {
                int count = 0;
                for (LessonRoute lr : lessonRoutes) {
                    UserLessonProgress ulp = userLessonProgressRep.findByLessonRoute_Id(lr.getId()).orElseThrow(() -> new RuntimeException("LessonRoute not found: " + lr.getId()));
                    if (ulp.isLearned()) count++;
                }
                if (count == lessonRoutes.size()) r.setLearned(true);
            }
            res.add(r);
        }
        return res;
    }
}
