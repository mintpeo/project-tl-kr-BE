package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class LessonRes {
    private long id;
    private Integer orderIndex;
    private String title;
    private String thumbnail;
    private String des;
    private String youtubeId;
}
