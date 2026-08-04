package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.AuthReq;
import com.atbm.projecttlkrbe.dto.request.AuthResetPassReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.service.AuthSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class AuthCon {
    private final AuthSer ser;

    @PatchMapping("/reset-pass")
    public AuthRes resetPass(@RequestBody AuthResetPassReq req) {
        return ser.resetPass(req);
    }

    @PostMapping("/signUp")
    public AuthRes signUp(@RequestBody AuthReq authReq){
        return ser.signUp(authReq);
    }

    @PostMapping("/login")
    public AuthRes login(@RequestBody AuthReq authReq){
        return ser.login(authReq);
    }
}
