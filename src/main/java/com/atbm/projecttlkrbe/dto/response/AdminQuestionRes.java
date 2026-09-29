package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AdminQuestionRes {
    private Long id;
    private Long quizId;
    private String question;
    private String questionMediaUrl;
    private List<AdminOptionRes> options;
}