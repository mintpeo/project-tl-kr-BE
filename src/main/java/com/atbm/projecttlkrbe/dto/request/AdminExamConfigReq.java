package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class AdminExamConfigReq {
    private Long id;
    private Integer numberOfConsonant;
    private Integer numberOfVowel;
    private Integer passPercent;
    private Integer timeLimitSec;
}
