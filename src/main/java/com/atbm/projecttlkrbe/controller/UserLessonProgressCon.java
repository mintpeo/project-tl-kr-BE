package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.CompleteLessonReq;
import com.atbm.projecttlkrbe.service.UserLessonProgressSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user-lesson-progress")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class UserLessonProgressCon {
    private final UserLessonProgressSer ser;

    @PostMapping("/complete")
    public boolean complete(@RequestBody CompleteLessonReq req) {
        return ser.setCompleteLesson(req);
    }

    @PostMapping("/create-road")
    public boolean createRoad(@RequestBody Map<String, Long> body) {
        long userId = body.get("userId");
        return ser.createUserLessonProgress(userId);
    }
}
