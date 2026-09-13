package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.PracticeReq;
import com.atbm.projecttlkrbe.model.Practice;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.repository.PracticeRep;
import com.atbm.projecttlkrbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeSer {
    private final PracticeRep rep;
    private final UserRep userRep;

    // Get All Practice
    public List<Practice> getAllPractice() {
        return rep.findAll();
    }

    // Save Practice
    public boolean savePractice(PracticeReq req) {
        Practice res = new Practice();
        User user = userRep.findById(req.getUserId()).orElseThrow(() -> new RuntimeException("User not found: " + req.getUserId()));

        res.setUser(user);
        res.setPredictedLabel(req.getPredictedLabel());
        res.setConfidence(req.getConfidence());
        res.setCharacterId(req.getCharacterId());
        res.setScore(req.getScore());
        res.setCreatedAt(LocalDateTime.now());

        // Is Pass
        res.setIsPassed(req.getScore() >= 65);
        rep.save(res);
        return true;
    }
}
