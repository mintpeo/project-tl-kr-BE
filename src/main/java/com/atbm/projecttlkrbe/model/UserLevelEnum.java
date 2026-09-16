package com.atbm.projecttlkrbe.model;

import lombok.Getter;

@Getter
public enum UserLevelEnum {
    ADVANCED(30, 85),
    INTERMEDIATE(15, 65),
    BEGINNER(0, 50),
    NOVICE(0, 0.0);

    private final int minQuestions;
    private final double minAccuracyRate;
    UserLevelEnum(int minQuestions, double minAccuracyRate) {
        this.minQuestions = minQuestions;
        this.minAccuracyRate = minAccuracyRate;
    }

    public static UserLevelEnum determineLevel(int totalQuestions, double totalAccuracyRate) {
        for (UserLevelEnum level: values()) {
            if (totalQuestions >= level.minQuestions && totalAccuracyRate >= level.minAccuracyRate) return level;
        }
        return NOVICE;
    }
}