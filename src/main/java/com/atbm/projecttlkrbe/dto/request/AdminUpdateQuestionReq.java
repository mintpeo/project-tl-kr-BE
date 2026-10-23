package com.atbm.projecttlkrbe.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AdminUpdateQuestionReq {
    private Long id;
    private Long quizId;
    private String question;
    private String questionMediaUrl;
    private Long correct;
    List<AdminUpdateOptionReq> options;

    @JsonProperty("isAudio")
    private boolean isAudio;
}
