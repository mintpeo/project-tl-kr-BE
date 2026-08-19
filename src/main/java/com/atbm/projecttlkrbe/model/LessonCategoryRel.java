package com.atbm.projecttlkrbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lesson_category_rel")
@Data
public class LessonCategoryRel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "lesson_category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private LessonCategory lessonCategory;

    @JoinColumn(name = "lesson_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Lesson lesson;
}
