package com.atbm.projecttlkrbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleRes {
    private Long id;
    private String imgUrl;
    private LocalDate uploadedAt;
}
