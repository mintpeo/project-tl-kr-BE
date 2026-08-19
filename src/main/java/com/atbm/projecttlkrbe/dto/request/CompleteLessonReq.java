package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class CompleteLessonReq {
    private long userId;
    private long lessonId;
}
