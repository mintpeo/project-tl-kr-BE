package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.DailyScoreReq;
import com.atbm.projecttlkrbe.dto.response.*;
import com.atbm.projecttlkrbe.model.UserLevel;
import com.atbm.projecttlkrbe.service.ProgressSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class ProgressCon {
    private final ProgressSer ser;

    @PostMapping("/get-user-level")
    public UserLevelRes getUserLevel(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        return ser.getUserLevel(userId);
    }

    @PostMapping("/user-level")
    public void setUserLevel(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        ser.setUserLevel(userId);
    }

    @PostMapping("/lesson-progress")
    public List<LessonProgressRes> getLessonProgress(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        return ser.getLessonProgress(userId);
    }

    @PostMapping("/master")
    public List<MasterRes> handleMasterList(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        return ser.handleMasterList(userId);
    }

    @PostMapping("/get-total-practice")
    public List<Double> getTotalPractice(@RequestBody DailyScoreReq req) {
        return ser.getTotalPractice(req);
    }

    @PostMapping("/get-score-date")
    public List<DailyScoreDateRes> getScoreDate(@RequestBody DailyScoreReq req) {
        return ser.getDaily(req);
    }

    @PostMapping("/get")
    public ProgressRes getProgressRes(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        return ser.getProgress(userId);
    }
}
