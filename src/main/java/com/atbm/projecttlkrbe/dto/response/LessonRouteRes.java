package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class LessonRouteRes {
    private long id;
    private String name;
    private long cateRouteId;
    private String youtubeId;
    private String des;
    private Integer orderIndex;
    private boolean isLearned;
}
