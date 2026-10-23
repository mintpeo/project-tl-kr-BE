package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class QuestionCorrectLevelExamRes {
    private int correct;
    private int total;
    private int percent;
    private boolean passed;
    private Map<String, ChapterRes> byChapter;
}
