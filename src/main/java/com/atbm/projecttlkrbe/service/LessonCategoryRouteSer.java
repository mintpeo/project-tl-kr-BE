package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.LessonCategoryRouteRes;
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
            // Result
            LessonCategoryRouteRes r = new LessonCategoryRouteRes();
            long cateRouteId = l.getId();
            r.setId(cateRouteId);
            r.setName(l.getName());
            r.setOrderIndex(l.getOrderIndex());
            r.setDes(l.getDescription());
            // Lesson Size
            List<LessonRoute> lessons = lessonRouteRep.findByCateRouteId(cateRouteId);
            r.setLessonsSize(lessons.size());
            res.add(r);
        }
        return res;
    }

    public List<LessonCategoryRouteRes> getCateRouteByUserId(long userId) {
        List<LessonCategoryRoute> list = rep.findAll();
        List<LessonCategoryRouteRes> res = new ArrayList<>();
        for (LessonCategoryRoute l : list) {
            // Result
            LessonCategoryRouteRes r = new LessonCategoryRouteRes();
            long cateRouteId = l.getId();
            r.setId(cateRouteId);
            r.setName(l.getName());
            r.setOrderIndex(l.getOrderIndex());
            r.setDes(l.getDescription());
            // Lesson Size
            List<LessonRoute> lessons = lessonRouteRep.findByCateRouteId(cateRouteId);
            r.setLessonsSize(lessons.size());
            // Is Learn Category
            if (lessons.isEmpty()) r.setLearned(false);
            else {
                int count = 0;
                for (LessonRoute lr : lessons) {
                    UserLessonProgress ulp = userLessonProgressRep.findByUser_IdAndLessonRoute_Id(userId, lr.getId()).orElseThrow(() -> new RuntimeException("LessonRoute not found: " + lr.getId()));
                    if (ulp.isLearned()) count++;
                }
                if (count == lessons.size()) r.setLearned(true);
            }

            res.add(r);
        }
        return res;
    }
}
