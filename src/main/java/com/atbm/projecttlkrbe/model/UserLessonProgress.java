package com.atbm.projecttlkrbe.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_lesson_progress")
@Data
public class UserLessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "lesson_id")
    @OneToOne(fetch = FetchType.LAZY)
    private LessonRoute lessonRoute;

    private boolean isLearned;
    private LocalDateTime completedAt;
}
