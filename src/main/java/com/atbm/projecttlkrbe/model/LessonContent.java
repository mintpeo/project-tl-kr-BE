package com.atbm.projecttlkrbe.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lesson_content")
@Data
public class LessonContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "lessonRoute_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private LessonRoute lessonRoute;

    private String name;
    private String description;
}
