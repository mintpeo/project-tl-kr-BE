package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.CreateUserAdminReq;
import com.atbm.projecttlkrbe.dto.request.UserChangeProfileReq;
import com.atbm.projecttlkrbe.dto.response.AdminUserRes;
import com.atbm.projecttlkrbe.model.*;
import com.atbm.projecttlkrbe.repository.*;
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
    private final UserLessonProgressRep userLessonProgressRep;
    private final ResetPassRep resetPassRep;
    private final VerifyRep verifyRep;
    private final PasswordEncoder passwordEncoder;
    private final UserStreakRep userStreakRep;
    private final PracticeRep practiceRep;
    private final LevelExamRep levelExamRep;
    private final UserLevelRep userLevelRep;
    private final QuizAttemptsRep quizAttemptsRep;

    // Delete User
    public boolean deleteUser(long authId) {
        Auth auth = authRep.findById(authId).orElseThrow(() -> new RuntimeException("Auth not found: " + authId));
        User user = userRep.findByAuthId(authId).orElseThrow(() -> new RuntimeException("User not found: " + authId));

        List<UserLessonProgress> list = userLessonProgressRep.findByUser_Id(user.getId());
        for (UserLessonProgress ulp : list) {
            userLessonProgressRep.delete(ulp);
        }
        // Reset Pass -> Auth
        List<ResetPass> resetPasses = resetPassRep.findByAuth_Id(authId);
        for (ResetPass rp : resetPasses) resetPassRep.delete(rp);
        // Verify -> Auth
        List<Verify> verifies = verifyRep.findByAuthId(authId);
        for (Verify vp : verifies) verifyRep.delete(vp);
        // User streak
        UserStreak userStreak = userStreakRep.findByUser_Id(user.getId());
        userStreakRep.delete(userStreak);
        // Practice
        List<Practice> practices = practiceRep.findByUser_Id(user.getId());
        for (Practice p : practices) practiceRep.delete(p);
        // Level exam
        List<LevelExam> levelExams = levelExamRep.findByUser_Id(user.getId());
        for (LevelExam lev : levelExams) levelExamRep.delete(lev);
        // User Lesson Progress
        List<UserLessonProgress> userLessonProgresses = userLessonProgressRep.findByUser_Id(user.getId());
        for (UserLessonProgress ulp : userLessonProgresses) userLessonProgressRep.delete(ulp);
        // User Level
        Optional<UserLevel> userLevel = userLevelRep.findByUserId(user.getId());
        userLevel.ifPresent(userLevelRep::delete);
        // Quiz attempts
        List<QuizAttempts> quizAttempts = quizAttemptsRep.findByUser_Id(user.getId());
        for (QuizAttempts qa : quizAttempts) quizAttemptsRep.delete(qa);

        userRep.delete(user);
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
