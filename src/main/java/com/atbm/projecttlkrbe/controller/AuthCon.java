package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.AuthChangePassReq;
import com.atbm.projecttlkrbe.dto.request.AuthReq;
import com.atbm.projecttlkrbe.dto.request.AuthResetPassReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.dto.response.UserRes;
import com.atbm.projecttlkrbe.service.AuthSer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    public UserRes loadUser(@AuthenticationPrincipal OAuth2User user) {
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
    public UserRes loginSession(@RequestBody AuthReq authReq, HttpServletRequest request) {
        return ser.loginWithSession(authReq, request);
    }

    @GetMapping("/load-user")
    public Authentication loadUserLogin(Authentication authentication) {
        return ser.loadUserLogin(authentication);
    }
}
