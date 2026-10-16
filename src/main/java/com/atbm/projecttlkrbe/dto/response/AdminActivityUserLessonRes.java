package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AdminActivityUserLessonRes {
    private double totalProgress;
    private int completedLessons;
    private int learningLessons;
    private List<AdminLessonUserRes> lessons;
}
