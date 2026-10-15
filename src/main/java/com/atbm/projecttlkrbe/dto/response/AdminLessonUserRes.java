package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class AdminLessonUserRes {
    private Long lessonId;
    private String name;
    private boolean status;
    private int progress;
    private AdminQuizActivityRes quiz;
}
