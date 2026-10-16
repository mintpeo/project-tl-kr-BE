package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AdminTempDataRes {
    private Long id;
    private String glyph;
    private String romanization;
    private String category;
    private List<SampleRes> samples;
}
