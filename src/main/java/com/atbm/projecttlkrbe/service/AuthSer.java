package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.AuthReq;
import com.atbm.projecttlkrbe.dto.request.AuthResetPassReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.model.Role;
import com.atbm.projecttlkrbe.repository.AuthRep;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthSer {
    private final AuthRep rep;
    private final VerifySer verifySer;
    private final PasswordEncoder passwordEncoder;

    // Reset Password
    public AuthRes resetPass(AuthResetPassReq req) {
        String email = req.getEmail();
        String password = req.getPassword();

        Auth auth = rep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));
        auth.setPassword(passwordEncoder.encode(password));
        rep.save(auth);

        AuthRes res = new AuthRes();
        res.setEmail(email);
        return res;
    }

    // Check email
    private boolean checkEmail(String email) {
        return rep.existsByEmail(email);
    }

    // Sign Up
    public AuthRes signUp(AuthReq authReq) {
        Optional<Auth> authEmail = rep.findByEmail(authReq.getEmail());
        if (authEmail.isPresent()) { // exists
            Auth existingAuth = authEmail.get();

            if (existingAuth.isEnabled()) { // enabled = true
                throw new RuntimeException("Email already exists");
            } else {
                verifySer.saveCode(existingAuth);

                AuthRes res = new AuthRes();
                res.setEmail(existingAuth.getEmail());
                return res;
            }
        }

        Auth auth = new Auth();
        auth.setEmail(authReq.getEmail());
        auth.setPassword(passwordEncoder.encode(authReq.getPassword()));
        auth.setName(authReq.getName());
        auth.setCreatedAt(LocalDateTime.now());
        auth.setEnabled(false);
        auth.setRole(Role.USER);
        Auth saveAuth = rep.save(auth);

        verifySer.saveCode(saveAuth);

        // Response
        AuthRes res = new AuthRes();
        res.setEmail(saveAuth.getEmail());
        return res;
    }

    // Login
    public AuthRes login(AuthReq authReq) {
        String email = authReq.getEmail();
        Auth auth = rep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));
        if (!passwordEncoder.matches(authReq.getPassword(), auth.getPassword())) throw new RuntimeException("Wrong password");
        if (!auth.isEnabled()) throw new RuntimeException("Auth is not enabled");

        // Success
        AuthRes res = new AuthRes();
        res.setId(auth.getId());
        res.setEmail(auth.getEmail());
        res.setName(auth.getName());
        res.setRole(auth.getRole().toString());

        return res;
    }
}
