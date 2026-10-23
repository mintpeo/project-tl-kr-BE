package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.model.LevelExam;
import com.atbm.projecttlkrbe.service.LevelExamSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/level-exam")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class LevelExamCon {
    private final LevelExamSer ser;

    @PostMapping("/get")
    public Optional<LevelExam> getLevelExam(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        return ser.getLevelExam(userId);
    }
}
