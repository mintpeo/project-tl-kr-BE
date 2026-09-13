package com.atbm.projecttlkrbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRes {
    private Long id;
    private String questionText;
    private String questionMediaUrl;
    private List<OptionRes> options;
}
