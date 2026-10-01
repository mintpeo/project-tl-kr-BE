package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class AdminStrokeOptionDataReq {
    private String content;
    private boolean accepted;
    private Long strokeId;
}