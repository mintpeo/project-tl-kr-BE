package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.PracticeReq;
import com.atbm.projecttlkrbe.model.Practice;
import com.atbm.projecttlkrbe.service.PracticeSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/practice")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class PracticeCon {
    private final PracticeSer ser;

    @GetMapping("/all")
    public List<Practice> getAllPractice() {
        return ser.getAllPractice();
    }

    @PostMapping("/save")
    public boolean savePractice(@RequestBody PracticeReq req) {
        return ser.savePractice(req);
    }
}
