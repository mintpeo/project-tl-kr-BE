package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.response.AdminUserRes;
import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.service.AdminSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class AdminCon {
    private final AdminSer ser;

    @GetMapping("/all")
    public List<AdminUserRes> getAllAuth() {
        return ser.getAllAuth();
    }
}
