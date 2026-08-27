package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.AuthChangePassReq;
import com.atbm.projecttlkrbe.dto.request.UserChangeProfileReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.dto.response.UserRes;
import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.model.Role;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.model.UserLessonProgress;
import com.atbm.projecttlkrbe.repository.AuthRep;
import com.atbm.projecttlkrbe.repository.UserLessonProgressRep;
import com.atbm.projecttlkrbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSer {
    private final UserRep rep;
    private final AuthRep authRep;
    private final UserLessonProgressRep userLessonProgressRep;
    private final PasswordEncoder passwordEncoder;

    // Get Info User
    public UserRes getInfoUser(String email) {
        Auth auth = authRep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));
        User user = rep.findByAuthId(auth.getId()).orElseThrow(() -> new RuntimeException("Not found authId: " + auth.getId()));
        return responseUser(auth, user);
    }

    // Set up User Res
    public UserRes responseUser(Auth auth, User user) {
        UserRes res = new UserRes();
        res.setId(auth.getId());
        res.setEmail(auth.getEmail());
        res.setFullName(user.getFullName());
        res.setRole(auth.getRole().toString());
        res.setGoogle(auth.isGoogle());
        res.setUserId(user.getId());

        // Is Lesson Road
        long lessonId = 1;
        Optional<UserLessonProgress> ulp = userLessonProgressRep.findByUser_IdAndLessonRoute_Id(user.getId(), lessonId);
        if (ulp.isPresent()) res.setLessonRoad(true);
        else res.setLessonRoad(false);

        return res;
    }

    // Change Pass in Profile
    public UserRes changePassword(AuthChangePassReq req) {
        String email = req.getEmail();
        String oldPassword = req.getOldPassword();
        String newPassword = req.getNewPassword();

        Auth auth = authRep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));
        if (passwordEncoder.matches(oldPassword, auth.getPassword())) {
            auth.setPassword(passwordEncoder.encode(newPassword));
            authRep.save(auth);
        } else throw new RuntimeException("Wrong password");

        UserRes res = new UserRes();
        res.setEmail(email);
        return res;
    }

    // Change Profile
    public boolean changeProfile(UserChangeProfileReq req) {
        String email = req.getEmail();
        String fullName = req.getFullName();
        String phone = req.getPhone();
        String role = req.getRole();
        Boolean isActive = req.getIsActive();

        Auth auth = authRep.findByEmail(email).orElseThrow(() -> new RuntimeException("Not found email: " + email));
        long authId = auth.getId();
        User user = rep.findByAuthId(authId).orElseThrow(() -> new RuntimeException("Not found authId: " + authId));

        if (fullName != null && !fullName.trim().isEmpty()) user.setFullName(fullName);
        if (phone != null && !phone.trim().isEmpty()) user.setNumberPhone(phone);
        if (role != null && !role.trim().isEmpty()) auth.setRole(Role.valueOf(role));
        if (isActive != null && isActive != auth.isEnabled()) auth.setEnabled(isActive);

        rep.save(user);
        return true;
    }
}