package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.VerifyEmailReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.service.VerifySer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/verify")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class VerifyCon {
    private final VerifySer ser;

    @PostMapping("/send-reset-pass")
    public boolean sendResetPass(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return ser.sendResetPassEmail(email);
    }

    @PostMapping("/email")
    public AuthRes verifyEmail(@RequestBody VerifyEmailReq req) {
        return ser.verifyCode(req);
    }

    @PostMapping("/resend-code")
    public AuthRes sendVerifyMailAgain(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return ser.sendVerifyEmailAgain(email);
    }
}
