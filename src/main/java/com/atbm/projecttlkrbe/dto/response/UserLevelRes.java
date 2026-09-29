package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class UserLevelRes {
    private String userLevel;
    private int levelNumber;
    private double accuracyRate;
    private int totalScore;
    private int totalQuestions;
    private String nextLevelTitle;
    private int remainingQuestions;
    private double remainingAccuracy;
    private double progressPercent;
}
