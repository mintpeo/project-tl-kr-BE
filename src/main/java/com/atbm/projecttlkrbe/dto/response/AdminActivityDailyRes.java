package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class AdminActivityDailyRes {
    private String date;
    private Long totalPractice;

    public AdminActivityDailyRes(String date, Long totalPractice) {
        this.date = date;
        this.totalPractice = totalPractice;
    }
}