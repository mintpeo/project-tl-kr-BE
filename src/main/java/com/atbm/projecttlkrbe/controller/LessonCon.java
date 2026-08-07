package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.model.Lesson;
import com.atbm.projecttlkrbe.service.LessonSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lesson")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class LessonCon {
    private final LessonSer ser;

    @GetMapping("/all")
    public List<Lesson> getAllLessons() {
        return ser.getAllLessons();
    }
}
