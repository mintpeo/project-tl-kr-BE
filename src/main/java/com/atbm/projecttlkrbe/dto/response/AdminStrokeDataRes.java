package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminStrokeDataRes {
    private Long id;
    private String activePublicId;
    private String activeUrl;
    private String note;
    private String pendingPublicId;
    private String pendingUrl;
    private String status;
    private LocalDate updatedAt;
    private String glyph;
    private String romanization;
    private Integer declaredStrokes;
}
