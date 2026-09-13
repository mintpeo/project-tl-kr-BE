package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class UserQuizAttemptsReq {
    private Long userId;
    private Long quizId;
}
