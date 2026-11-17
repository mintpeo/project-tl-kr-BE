package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class AnswersReq {
    private Long questionId;
    private Long optionId;
    private String chapter;
}
