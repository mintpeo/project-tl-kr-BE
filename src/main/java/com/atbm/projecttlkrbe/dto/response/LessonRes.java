package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class LessonRes {
    private long id;
    private int orderIndex;
    private String title;
    private String thumbnail;
}
