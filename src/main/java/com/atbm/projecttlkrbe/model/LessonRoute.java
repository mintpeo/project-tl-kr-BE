package com.atbm.projecttlkrbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "lesson_route")
@Data
public class LessonRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "cateRoute_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private LessonCategoryRoute cateRoute;

    private String name;
    private String description;
    private int orderIndex;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String youtubeId;
    private String duration;
}
