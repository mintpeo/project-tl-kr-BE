package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.LessonCategoryRouteRes;
import com.atbm.projecttlkrbe.dto.response.LessonRouteRes;
import com.atbm.projecttlkrbe.model.LessonCategoryRoute;
import com.atbm.projecttlkrbe.model.LessonRoute;
import com.atbm.projecttlkrbe.repository.LessonCategoryRouteRep;
import com.atbm.projecttlkrbe.repository.LessonRouteRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonCategoryRouteSer {
    private final LessonCategoryRouteRep rep;
    private final LessonRouteRep lessonRouteRep;

    // Get lesson with category route id
    public List<LessonRouteRes> getLessonWithCateRouteId(long cateRouteId) {
        List<LessonRoute> list = lessonRouteRep.findByCateRouteId(cateRouteId);
        List<LessonRouteRes> res = new ArrayList<>();
        for (LessonRoute lr : list) {
            LessonRouteRes lrr = new LessonRouteRes();
            lrr.setId(lr.getId());
            lrr.setName(lr.getName());
            lrr.setYoutubeId(lr.getYoutubeId());
            lrr.setCateRouteId(cateRouteId);
            res.add(lrr);
        }
        return res;
    }

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
            res.add(r);
        }
        return res;
    }
}
