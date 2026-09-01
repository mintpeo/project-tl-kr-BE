package com.atbm.projecttlkrbe.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddLessonRouteReq {
    private String name;
    private long cateRouteId;
    private Integer orderIndex;
    private String duration;
    private String description;
    private String youtubeId;

    @JsonProperty("isActive")
    private boolean isActive;
}
