package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class AddLessonRouteReq {
    private String name;
    private long cateRouteId;
    private Integer orderIndex;
    private boolean isActive;
    private String duration;
    private String description;
    private String youtubeId;
}
