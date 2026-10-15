package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class AdminLessonActivityRes {
    private Long lessonId;
    private String lessonName;
    private int totalUser;
    private int totalUserStarted;
    private int totalUserCompleted;
}
