package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class UploadFileCharacterReq {
    private Long charId;
    private String targetFileName;
}
