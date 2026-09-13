package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.DailyScoreReq;
import com.atbm.projecttlkrbe.dto.response.DailyScoreDateRes;
import com.atbm.projecttlkrbe.dto.response.LessonProgressRes;
import com.atbm.projecttlkrbe.dto.response.MasterRes;
import com.atbm.projecttlkrbe.dto.response.ProgressRes;
import com.atbm.projecttlkrbe.service.ProgressSer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class ProgressCon {
    private final ProgressSer ser;

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
