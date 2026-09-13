package com.atbm.projecttlkrbe.dto.response;

import java.time.LocalDate;

public interface DailyScoreDateRes {
    LocalDate getDate();
    Double getAverageScore();
    Long getTotalPractice();
}