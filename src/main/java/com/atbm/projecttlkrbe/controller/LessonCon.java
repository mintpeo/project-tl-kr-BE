package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.response.LessonListRes;
import com.atbm.projecttlkrbe.dto.response.VideoByLessonIdRes;
import com.atbm.projecttlkrbe.model.Lesson;
import com.atbm.projecttlkrbe.service.LessonSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class LessonCon {
    private final LessonSer ser;

    @GetMapping("/lessons-custom")
    public List<LessonListRes> getLessonList() {
        return ser.getLessonList();
    }

    @GetMapping("/{lessonId}")
    public VideoByLessonIdRes getVideoByLessonId(@PathVariable("lessonId") Long lessonId) {
        return ser.getLessonById(lessonId);
    }

    @GetMapping("/all")
    public List<Lesson> getAllLessons() {
        return ser.getAllLessons();
    }
}
