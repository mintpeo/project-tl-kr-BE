package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.response.LessonCategoryRouteRes;
import com.atbm.projecttlkrbe.dto.response.LessonRouteRes;
import com.atbm.projecttlkrbe.service.LessonCategoryRouteSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lesson-cate-route")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class LessonCategoryRouteCon {
    private final LessonCategoryRouteSer ser;

    @PostMapping("/lessons")
    public List<LessonRouteRes> getLessonByCateRouteId(@RequestBody Map<String, Long> body) {
        Long cateRouteId = body.get("cateRouteId");
        return ser.getLessonWithCateRouteId(cateRouteId);
    }

    @GetMapping("/all")
    public List<LessonCategoryRouteRes> getAll() {
        return ser.getCateRoute();
    }
}
