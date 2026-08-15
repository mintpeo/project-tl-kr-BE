package com.atbm.projecttlkrbe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lesson_category_route")
@Data
public class LessonCategoryRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer orderIndex;
    private String description;
}
