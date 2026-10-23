package com.atbm.projecttlkrbe.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AdminCreateQuestionReq {
    private Long quizId;
    private String question;
    private String questionMediaUrl;
    private int correct;
    private List<AdminCreateOptionReq> options;

    @JsonProperty("isAudio")
    private boolean isAudio;
}
