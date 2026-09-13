package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class ProgressRes {
    private Double averageScore;
    private int countPractices;
    private int countIsPass;
}
