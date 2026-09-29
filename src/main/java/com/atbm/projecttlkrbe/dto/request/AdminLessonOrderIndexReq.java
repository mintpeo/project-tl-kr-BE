package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AdminLessonOrderIndexReq {
    private List<Long> lessonsId;
    private Long cateRouteId;
}
