package com.atbm.projecttlkrbe.dto.response;

public interface LowestCharScoreRes {
    Long getCharacterId();
    String getPredictedLabel();
    Double getAverageScore();
    Long getTotalAttempts();
}
