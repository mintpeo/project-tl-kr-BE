package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ListQuestionsReq {
    private Long userId;
    private List<AnswersReq> answers;
}
