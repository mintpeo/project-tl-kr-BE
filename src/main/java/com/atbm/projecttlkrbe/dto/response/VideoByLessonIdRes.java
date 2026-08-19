package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class VideoByLessonIdRes {
    private long id;
    private String title;
    private String youtubeId;
}
