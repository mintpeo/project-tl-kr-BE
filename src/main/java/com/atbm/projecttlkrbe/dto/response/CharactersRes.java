package com.atbm.projecttlkrbe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharactersRes {
    private long id;
    private String name;
    private String transcription;
    private String strokeUrl;
}
