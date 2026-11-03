package com.atbm.projecttlkrbe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "exam_config")
@Data
public class ExamConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer passPercent;
    private Integer timeLimitSec;
    private Integer numberOfVowel;
    private Integer numberOfConsonant;
}
