package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.LessonRouteReq;
import com.atbm.projecttlkrbe.dto.response.LessonRouteRes;
import com.atbm.projecttlkrbe.service.LessonRouteSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson-route")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class LessonRouteCon {
    private final LessonRouteSer ser;

    @PostMapping("/lessons")
    public List<LessonRouteRes> getLessonByCateRouteId(@RequestBody LessonRouteReq req) {
        return ser.getLessonWithCateRouteId(req);
    }
}
