package com.atbm.projecttlkrbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizRes {
    private Long id;
    private String title;
    private String description;
    private List<QuestionRes> questions;
}
