package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.CreateUserAdminReq;
import com.atbm.projecttlkrbe.dto.response.AdminUserRes;
import com.atbm.projecttlkrbe.service.AdminSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class AdminCon {
    private final AdminSer ser;

    @DeleteMapping("/delete")
    public boolean deleteUser(@RequestBody Map<String, Long> body) {
        long authId = body.get("authId");
        return ser.deleteUser(authId);
    }

    @PostMapping("/create")
    public boolean createUser(@RequestBody CreateUserAdminReq req) {
        return ser.createUser(req);
    }

    @PostMapping("/email")
    public List<AdminUserRes> getAdminUserByEmail(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return ser.getAdminUsersByEmail(email);
    }

    @GetMapping("/all")
    public List<AdminUserRes> getAllAuth() {
        return ser.getAllAuth();
    }
}
