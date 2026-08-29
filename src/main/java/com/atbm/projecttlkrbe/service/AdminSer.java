package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.CreateUserAdminReq;
import com.atbm.projecttlkrbe.dto.request.UserChangeProfileReq;
import com.atbm.projecttlkrbe.dto.response.AdminUserRes;
import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.model.LessonRoute;
import com.atbm.projecttlkrbe.model.Role;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.repository.AuthRep;
import com.atbm.projecttlkrbe.repository.LessonRouteRep;
import com.atbm.projecttlkrbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminSer {
    private final AuthRep authRep;
    private final UserRep userRep;
    private final UserSer userSer;
    private final PasswordEncoder passwordEncoder;

    // Delete User
    public boolean deleteUser(long authId) {
        Auth auth = authRep.findById(authId).orElseThrow(() -> new RuntimeException("Auth not found: " + authId));
        userRep.findByAuthId(auth.getId()).ifPresent(userRep::delete);
        authRep.delete(auth);
        return true;
    }

    // Create New User
    public boolean createUser(CreateUserAdminReq req) {
        Optional<Auth> auth = authRep.findByEmail(req.getEmail());
        if (auth.isPresent()) {
            System.out.println("Email already exists");
            return false;
        }

        Auth newAuth = new Auth();
        newAuth.setEmail(req.getEmail());
        newAuth.setPassword(passwordEncoder.encode(req.getPassword()));
        newAuth.setRole(Role.valueOf(req.getRole()));
        newAuth.setCreatedAt(LocalDateTime.now());
        newAuth.setEnabled(req.isActive());
        newAuth.setGoogle(false);
        Auth saveAuth = authRep.save(newAuth);

        User newUser = new User();
        newUser.setAuth(saveAuth);
        newUser.setFullName(req.getFullName());
        newUser.setNumberPhone(req.getNumberPhone());
        userRep.save(newUser);
        return true;
    }

    // Change Profile User
    public boolean changeProfileUser(UserChangeProfileReq req) {
        return userSer.changeProfile(req);
    }

    // Search User By Email
    public List<AdminUserRes> getAdminUsersByEmail(String email) {
        List<AdminUserRes> res = new ArrayList<>();
        List<Auth> auths = authRep.findByEmailContainingIgnoreCase(email);
        for (Auth auth : auths) {
            long authId = auth.getId();
            User user = userRep.findByAuthId(authId).orElseThrow(() -> new RuntimeException("User not found: " + authId));
            AdminUserRes a = getAdminUserRes(auth, user);
            res.add(a);
        }
        return res;
    }

    // Get List User for Admin
    public List<AdminUserRes> getAllAuth() {
        List<Auth> auth = authRep.findAll();
        List<AdminUserRes> res = new ArrayList<>();
        for (Auth authItem : auth) {
            long authId = authItem.getId();
            // User
            User user = userRep.findByAuthId(authId).orElseThrow(() -> new RuntimeException("User not found: " + authId));
            AdminUserRes a = getAdminUserRes(authItem, user);
            res.add(a);
        }
        return res;
    }

    // Get Admin Response
    private AdminUserRes getAdminUserRes(Auth auth, User user) {
        AdminUserRes res = new AdminUserRes();
        long authId = auth.getId();
        // Auth
        res.setAuthId(authId);
        res.setMail(auth.getEmail());
        res.setRole(auth.getRole().toString());
        res.setAuthCreateAt(auth.getCreatedAt());
        res.setActive(auth.isEnabled());
        res.setGoogle(auth.isGoogle());

        // User
        res.setUserId(user.getId());
        res.setFullName(user.getFullName());
        res.setPhone(user.getNumberPhone());

        return res;
    }
}
