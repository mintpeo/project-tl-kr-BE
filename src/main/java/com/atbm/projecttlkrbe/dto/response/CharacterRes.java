package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class CharacterRes {
    private Long id;
    private boolean isDouble;
    private String name;
    private Integer strokeCount;
    private String transcription;
    private String type;
    private String imgUrl;
    private String fileName;
    private String status;
    private String pendingUrl;
    private String pendingPublicId;
}
