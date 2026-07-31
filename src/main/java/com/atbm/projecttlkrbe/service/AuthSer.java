package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.AuthReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.model.Role;
import com.atbm.projecttlkrbe.repository.AuthRep;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthSer {
    private final AuthRep rep;
    private final PasswordEncoder passwordEncoder;

    // Check email
    private boolean checkEmail(String email) {
        return rep.existsByEmail(email);
    }

    // Sign Up
    public AuthRes signUp(AuthReq authReq) {
        if (checkEmail(authReq.getEmail())) throw new RuntimeException("Email already exists");

        Auth auth = new Auth();
        auth.setEmail(authReq.getEmail());
        auth.setName(authReq.getName());
        auth.setRole(Role.USER);

        String password = authReq.getPassword();
        auth.setPassword(passwordEncoder.encode(password));
        Auth saveAuth = rep.save(auth);

        AuthRes res = new AuthRes();
        res.setId(saveAuth.getId());
        res.setEmail(saveAuth.getEmail());

        return res;
    }

    // Login
    public AuthRes login(AuthReq authReq) {
        String email = authReq.getEmail();
        Auth auth = rep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));
        if (!passwordEncoder.matches(authReq.getPassword(), auth.getPassword())) throw new RuntimeException("Wrong password");

        // Success
        AuthRes res = new AuthRes();
        res.setId(auth.getId());
        res.setEmail(auth.getEmail());
        res.setName(auth.getName());

        return res;
    }
}
