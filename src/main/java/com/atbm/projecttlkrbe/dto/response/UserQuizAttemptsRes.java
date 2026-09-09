package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserQuizAttemptsRes {
    private boolean isQuizAttempt;
    private int score;
    private LocalDate date;
}
