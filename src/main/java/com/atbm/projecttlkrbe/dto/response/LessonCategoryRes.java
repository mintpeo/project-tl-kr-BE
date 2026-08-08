package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class LessonCategoryRes {
    private long id;
    private int orderIndex;
    private String categoryName;
    private String categoryDescription;
    private int lessonsLength;
    private List<LessonRes> lessons;
}
