package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.LessonRouteReq;
import com.atbm.projecttlkrbe.dto.response.LessonRouteRes;
import com.atbm.projecttlkrbe.model.LessonRoute;
import com.atbm.projecttlkrbe.model.UserLessonProgress;
import com.atbm.projecttlkrbe.repository.LessonRouteRep;
import com.atbm.projecttlkrbe.repository.UserLessonProgressRep;
import com.atbm.projecttlkrbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonRouteSer {
    private final LessonRouteRep rep;
    private final UserRep userRep;
    private final UserLessonProgressRep userLessonProgressRep;

    // Get lesson with category route id
    public List<LessonRouteRes> getLessonWithCateRouteId(LessonRouteReq req) {
        long userId = req.getUserId();
        long cateRouteId = req.getCateRouteId();

        List<LessonRoute> list = rep.findByCateRouteId(cateRouteId);
        List<LessonRouteRes> res = new ArrayList<>();
        for (LessonRoute lr : list) {
            // Lesson
            LessonRouteRes lrr = new LessonRouteRes();
            long lessonId = lr.getId();

            lrr.setId(lessonId);
            lrr.setName(lr.getName());
            lrr.setYoutubeId(lr.getYoutubeId());
            lrr.setCateRouteId(cateRouteId);
            lrr.setDes(lr.getDescription());
            lrr.setOrderIndex(lr.getOrderIndex());

            UserLessonProgress ulp = userLessonProgressRep.findByUser_IdAndLessonRoute_Id(userId, lessonId).orElseThrow(() -> new RuntimeException("Lesson Progress Not Found: " + userId + " " + lessonId));
            lrr.setLearned(ulp.isLearned());

            res.add(lrr);
        }
        return res;
    }
}
