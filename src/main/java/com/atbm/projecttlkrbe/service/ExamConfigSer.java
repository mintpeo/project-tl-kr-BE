package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.AdminExamConfigReq;
import com.atbm.projecttlkrbe.model.ExamConfig;
import com.atbm.projecttlkrbe.repository.ExamConfigRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamConfigSer {
    private final ExamConfigRep rep;

    // Edit Exam Config
    public boolean editExamConfig(AdminExamConfigReq req) {
        Optional<ExamConfig> examConfig = rep.findById(req.getId());
        if (examConfig.isEmpty()) return false;
        ExamConfig config = examConfig.get();
        config.setPassPercent(req.getPassPercent());
        config.setTimeLimitSec(req.getTimeLimitSec());
        config.setNumberOfVowel(req.getNumberOfVowel());
        config.setNumberOfConsonant(req.getNumberOfConsonant());
        rep.save(config);
        return true;
    }

    // Setup Exam Config
    public ExamConfig getExamConfig() {
        return rep.findAll().stream().findFirst().get();
    }
}
