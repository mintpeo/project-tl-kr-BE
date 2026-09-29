package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class AdminOptionRes {
    private Long id;
    private String optionText;
    private boolean correct;
}