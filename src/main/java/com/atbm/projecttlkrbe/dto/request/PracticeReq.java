package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class PracticeReq {
    private Long userId;
    private String predictedLabel;
    private Double confidence;
    private Long characterId;
    private Integer score;
}