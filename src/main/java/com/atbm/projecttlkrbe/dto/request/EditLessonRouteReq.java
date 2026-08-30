package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class EditLessonRouteReq {
    private long id;
    private String name;
    private Long cateRouteId;
    private int orderIndex;
    private Boolean isActive;
    private String duration;
    private String description;
    private String youtubeId;
}
