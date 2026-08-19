package com.atbm.projecttlkrbe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lesson_category")
@Data
public class LessonCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Integer orderIndex;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "lessonCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonCategoryRel> lessonCategoryRels = new ArrayList<>();
}
