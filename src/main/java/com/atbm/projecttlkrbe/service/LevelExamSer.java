package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.model.LevelExam;
import com.atbm.projecttlkrbe.repository.LevelExamRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LevelExamSer {
    private final LevelExamRep rep;

    // Get Level Exam
    public Optional<LevelExam> getLevelExam(Long userId) {
        Optional<LevelExam> levelExam = rep.findFirstByUser_IdOrderByPercentDescIdDesc(userId);
        if (levelExam.isPresent()) {
            LevelExam level = levelExam.get();
            if (level.isPassed()) return levelExam;
        }
        return Optional.empty();
    }
}
