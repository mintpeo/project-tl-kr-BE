package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class QuizAttemptsReq {
    private Long userId;
    private Long quizId;
    private int score;
    private int totalQuestions;
}
