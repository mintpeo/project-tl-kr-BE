package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.AdminExamConfigReq;
import com.atbm.projecttlkrbe.model.ExamConfig;
import com.atbm.projecttlkrbe.service.ExamConfigSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class ExamConfigCon {
    private final ExamConfigSer ser;

    @GetMapping("/get")
    public ExamConfig getExamConfig() {
        return ser.getExamConfig();
    }
}
