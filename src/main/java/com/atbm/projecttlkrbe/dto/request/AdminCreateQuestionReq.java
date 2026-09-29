package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AdminCreateQuestionReq {
    private Long quizId;
    private String question;
    private String questionMediaUrl;
    private int correct;
    private List<AdminCreateOptionReq> options;
}
