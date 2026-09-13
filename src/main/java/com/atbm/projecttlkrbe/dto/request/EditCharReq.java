package com.atbm.projecttlkrbe.dto.request;

import com.atbm.projecttlkrbe.model.CharacterType;
import lombok.Data;

@Data
public class EditCharReq {
    private long charId;
    private String name;
    private String transcription;
    private Boolean isDouble;
    private CharacterType type;
    private Integer strokeCount;
    private String strokeSvgUrl;
}
