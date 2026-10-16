package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetProgressPracticeTodayRes {
    private LocalDate today;
    private int totalPractice;
    private double totalScore;
}
