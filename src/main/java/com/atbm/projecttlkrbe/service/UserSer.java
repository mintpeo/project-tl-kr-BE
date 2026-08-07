package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.AuthChangePassReq;
import com.atbm.projecttlkrbe.dto.request.UserChangeProfileReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.repository.AuthRep;
import com.atbm.projecttlkrbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSer {
    private final UserRep rep;
    private final AuthRep authRep;
    private final PasswordEncoder passwordEncoder;

    // Get Info User
    public AuthRes getInfoUser(String email) {
        Auth auth = authRep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));
        User user = rep.findByAuthId(auth.getId()).orElseThrow(() -> new RuntimeException("Not found authId: " + auth.getId()));
        return responseUser(auth, user);
    }

    // Set up AuthRes
    private AuthRes responseUser(Auth auth, User user) {
        AuthRes res = new AuthRes();
        res.setId(auth.getId());
        res.setEmail(auth.getEmail());
        res.setFullName(user.getFullName());
        res.setRole(auth.getRole().toString());
        res.setGoogle(auth.isGoogle());
        return res;
    }

    // Change Pass in Profile
    public AuthRes changePassword(AuthChangePassReq req) {
        String email = req.getEmail();
        String oldPassword = req.getOldPassword();
        String newPassword = req.getNewPassword();

        Auth auth = authRep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));
        if (passwordEncoder.matches(oldPassword, auth.getPassword())) {
            auth.setPassword(passwordEncoder.encode(newPassword));
            authRep.save(auth);
        } else throw new RuntimeException("Wrong password");

        AuthRes res = new AuthRes();
        res.setEmail(email);
        return res;
    }

    // Change Profile
    public boolean changeProfile(UserChangeProfileReq req) {
        String email = req.getEmail();
        String fullName = req.getFullName();

        Auth auth = authRep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));
        long authId = auth.getId();
        User user = rep.findByAuthId(authId).orElseThrow(() -> new RuntimeException("Not found authId: " + authId));
        user.setFullName(fullName);
        rep.save(user);
        return true;
    }
}