package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.repository.AuthRep;
import com.atbm.projecttlkrbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSer {
    private final UserRep rep;
    private final AuthRep authRep;

    // Change Profile
    public boolean changeProfile(String email, String fullName) {
        Auth auth = authRep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));

        long authId = auth.getId();
        User user = rep.findByAuthId(authId).orElseThrow(() -> new RuntimeException("Not found authId: " + authId));
        user.setFullName(fullName);
        rep.save(user);
        return true;
    }
}
