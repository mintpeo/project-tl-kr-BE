package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class AdminActivityUseRes {
    private String name;
    private String email;
    private int totalPractices;
    private double avgScore;
    private int streak;
    private boolean active;
}
