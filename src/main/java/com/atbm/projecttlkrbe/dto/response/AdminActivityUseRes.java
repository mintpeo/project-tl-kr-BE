package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AdminActivityUseRes {
    private String name;
    private String email;
    private int totalPractices;
    private double avgScore;
    private int streak;
    private boolean active;
    private LocalDate lastActivityDate;
    private List<AdminActivityUserLessonRes> learningProgress;
    private List<MasterRes> handwriting;
    private List<DailyScoreDateRes> attendance;
    private UserLevelRes level;
}
