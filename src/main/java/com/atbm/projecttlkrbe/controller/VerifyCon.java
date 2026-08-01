package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.VerifyEmailReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.service.VerifySer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verify")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class VerifyCon {
    private final VerifySer ser;

    @PostMapping("/email")
    public AuthRes verifyEmail(@RequestBody VerifyEmailReq req) {
        return ser.verifyCode(req);
    }
}
