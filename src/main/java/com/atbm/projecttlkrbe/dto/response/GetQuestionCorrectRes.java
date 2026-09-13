package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class GetQuestionCorrectRes {
    private boolean isCorrect;
    private Long correctAnswer;
}
