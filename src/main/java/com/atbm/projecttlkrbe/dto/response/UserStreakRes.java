package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserStreakRes {
    private int currentStreak;
    private int longestStreak;
    private LocalDate lastCheckInDate;
    private boolean isChecked;
}
