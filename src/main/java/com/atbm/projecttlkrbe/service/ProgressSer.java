package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.DailyScoreReq;
import com.atbm.projecttlkrbe.dto.response.*;
import com.atbm.projecttlkrbe.model.*;
import com.atbm.projecttlkrbe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressSer {
    private final PracticeRep practiceRep;
    private final UserRep userRep;
    private final CharacterRep characterRep;
    private final LessonCategoryRouteSer lessonCategoryRouteSer;
    private final LessonRouteSer lessonRouteSer;
    private final QuizAttemptsRep quizAttemptsRep;
    private final UserLevelRep userLevelRep;

    // Get User Level
    public UserLevelRes getUserLevel(Long userId) {
        UserLevel userLevel = userLevelRep.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));
        int totalScore = userLevel.getTotalScore();
        int totalQuestions = userLevel.getTotalQuestions();
        double accuracyRate = userLevel.getAccuracyRate();
        double formattedRate = Math.round(accuracyRate * 100.0) / 100.0;

        UserLevelRes res = new UserLevelRes();
        // Level
        switch (userLevel.getLevel()) {
            case NOVICE -> {
                res.setUserLevel("Mới bắt đầu");
                res.setLevelNumber(1);
                res.setNextLevelTitle("Tiến độ lên Level 2");
                res.setRemainingQuestions(0);
                res.setRemainingAccuracy(50);
                res.setProgressPercent(Math.min(100, (accuracyRate / 50) * 100));
            }
            case BEGINNER -> {
                res.setUserLevel("Cơ bản");
                res.setLevelNumber(2);
                res.setNextLevelTitle("Tiến độ lên Level 3");
                int target = 15;
                res.setRemainingQuestions(Math.max(0, target - totalQuestions));
                res.setRemainingAccuracy(65);
                res.setProgressPercent(Math.min(100, ((double) totalQuestions / target) * 100));
            }
            case INTERMEDIATE -> {
                res.setUserLevel("Trung cấp");
                res.setLevelNumber(3);
                res.setNextLevelTitle("Tiến độ lên Level 4");
                int minTarget = 15;
                int maxTarget = 30;
                res.setRemainingQuestions(Math.max(0, maxTarget - totalQuestions));
                res.setRemainingAccuracy(85);
                double progress = ((double) (totalQuestions - minTarget) / (maxTarget - minTarget)) * 100.0;
                res.setProgressPercent(Math.min(100, progress));
            }
            case ADVANCED ->  {
                res.setUserLevel("Nâng cao");
                res.setLevelNumber(4);
                res.setNextLevelTitle("Đã đạt cấp tối đa");
                res.setRemainingQuestions(0);
                res.setRemainingAccuracy(0);
                res.setProgressPercent(100.0);
            }
        }

        res.setTotalScore(totalScore);
        res.setTotalQuestions(totalQuestions);
        res.setAccuracyRate(formattedRate);
        return res;
    }

    // Set User Level
    public void setUserLevel(Long userId) {
        // Check User
        User user = userRep.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Get total quiz id
        List<Long> nums = quizAttemptsRep.findDistinctQuizIdsByUserId(userId);
        int score = 0;
        int totalQuestions = 0;
        for (Long num : nums) {
            Optional<QuizAttempts> quizAttempts = quizAttemptsRep.findFirstByUserIdAndQuizIdOrderByIdDesc(userId, num);
            if (quizAttempts.isEmpty()) continue;
            QuizAttempts q = quizAttempts.get();
            score += q.getScore();
            totalQuestions += q.getTotalQuestions();
        }

        // Save User Level
        UserLevel userLevel = userLevelRep.findByUserId(userId)
                .orElseGet(() -> {
                    UserLevel ul = new UserLevel();
                    ul.setUser(user);
                    userLevelRep.save(ul);
                    return ul;
                });

        if (totalQuestions == 0) {
            userLevel.setLevel(UserLevelEnum.NOVICE);
            userLevel.setAccuracyRate(0.0);
            userLevel.setTotalScore(0);
            userLevel.setTotalQuestions(0);
            userLevelRep.save(userLevel);
            return;
        }

        double accuracyRate = ((double) score / totalQuestions) * 100;

        UserLevelEnum level = UserLevelEnum.determineLevel(totalQuestions, accuracyRate);
        userLevel.setLevel(level);

        userLevel.setAccuracyRate(accuracyRate);
        userLevel.setTotalScore(score);
        userLevel.setTotalQuestions(totalQuestions);
        userLevelRep.save(userLevel);
    }

    // Lesson Progress
    public List<LessonProgressRes> getLessonProgress(Long userId) {
        List<LessonProgressRes> res = new ArrayList<>();

        List<LessonCategoryRouteRes> categoryRoutes = lessonCategoryRouteSer.getCateRouteByUserId(userId);
        for (LessonCategoryRouteRes lcrr : categoryRoutes) {
            LessonProgressRes lps = new LessonProgressRes();
            lps.setNameCate(lcrr.getName());
            lps.setDesCate(lcrr.getDes());
            lps.setSizeCate(lcrr.getLessonsSize());

            // Count Lesson is Learned
            List<LessonRouteRes> routeRes = lessonRouteSer.getLessonWithCateRouteIdWithParm(userId, lcrr.getId());
            int count = 0;
            for (LessonRouteRes lrr : routeRes) {
                if (lrr.isLearned()) count++;
            }
            lps.setLearnLesson(count);

            res.add(lps);
        }
        return res;
    }

    // Handle Master List
    public List<MasterRes> handleMasterList(Long userId) {
        User user = userRep.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));
        // Xu ly ghi de lien tuc
        List<Practice> practices = practiceRep.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<CharacterEntity> characters = characterRep.findAll();

        // Master = [[name, score]]
        List<MasterRes> res = new ArrayList<>();
        for (CharacterEntity c : characters) {
            MasterRes mr = new MasterRes();
            mr.setNameChar(c.getName());
            mr.setScore(0);

            for(Practice p : practices) {
                if (p.getCharacterId() == null || !p.getCharacterId().equals(c.getId()) || p.getScore() == null) continue;
                mr.setScore(p.getScore());
                break;
            }

            res.add(mr);
        }
        return res;
    }

    // Get Total Practice
    public List<Double> getTotalPractice(DailyScoreReq req) {
        List<DailyScoreDateRes> dateRes = practiceRep.getDailyAccuracySince(req.getUserId(), req.getDays());
        return dateRes.stream().map(res -> res.getTotalPractice() != null ? res.getTotalPractice().doubleValue() : 0.0)
                .toList();
    }

    // Get Daily
    public List<DailyScoreDateRes> getDaily(DailyScoreReq req) {
        return practiceRep.getDailyAccuracySince(req.getUserId(), req.getDays());
    }

    // Progress Res
    public ProgressRes getProgress(long userId) {
        User user = userRep.findById(userId).orElseThrow(() -> new RuntimeException("user not found: " + userId));

        Long id = user.getId();
        Double scoreAVG = practiceRep.calculateAverageScoreByUserId(id);
        int countPractices = practiceRep.countByUser_Id(id);
        int countIsPass = practiceRep.countByUser_IdAndIsPassedTrue(id);

        ProgressRes res = new ProgressRes();
        res.setCountPractices(countPractices);
        res.setCountIsPass(countIsPass);
        res.setAverageScore(scoreAVG);
        return res;
    }
}