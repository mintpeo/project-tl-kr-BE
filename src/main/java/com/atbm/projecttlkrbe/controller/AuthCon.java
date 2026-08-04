package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.AuthReq;
import com.atbm.projecttlkrbe.dto.request.AuthResetPassReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.service.AuthSer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.frontend.url}", allowCredentials = "true")
@RequiredArgsConstructor
public class AuthCon {
    private final AuthSer ser;

    @GetMapping("/me")
    public OAuth2User currentUser(@AuthenticationPrincipal OAuth2User user) {
        return ser.loginGoogle(user);
    }

    @GetMapping("/go-info")
    public AuthRes loadUser(@AuthenticationPrincipal OAuth2User user) {
        return ser.loadUserGoogle(user);
    }

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
