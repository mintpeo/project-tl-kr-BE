package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class DailyScoreReq {
    private Long userId;
    private int days;
}
