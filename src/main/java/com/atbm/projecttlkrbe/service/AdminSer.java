package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.AdminUserRes;
import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.repository.AuthRep;
import com.atbm.projecttlkrbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSer {
    private final AuthRep authRep;
    private final UserRep userRep;

    public List<AdminUserRes> getAllAuth() {
        List<Auth> auth = authRep.findAll();
        List<AdminUserRes> res = new ArrayList<>();
        for (Auth authItem : auth) {
            AdminUserRes a = new AdminUserRes();
            long authId = authItem.getId();

            // Auth
            a.setAuthId(authId);
            a.setMail(authItem.getEmail());
            a.setRole(authItem.getRole().toString());
            a.setAuthCreateAt(authItem.getCreatedAt());
            a.setActive(authItem.isEnabled());
            a.setGoogle(authItem.isGoogle());

            // User
            User user = userRep.findByAuthId(authId).orElseThrow(() -> new RuntimeException("User not found: " + authId));
            a.setUserId(user.getId());
            a.setFullName(user.getFullName());
            a.setPhone(user.getNumberPhone());

            res.add(a);
        }
        return res;
    }
}
