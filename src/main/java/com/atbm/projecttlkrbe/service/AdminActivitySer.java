package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.DailyScoreReq;
import com.atbm.projecttlkrbe.dto.response.*;
import com.atbm.projecttlkrbe.model.*;
import com.atbm.projecttlkrbe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminActivitySer {
    private final AuthRep authRep;
    private final UserRep userRep;
    private final PracticeRep practiceRep;
    private final UserStreakRep userStreakRep;
    private final UserLevelRep userLevelRep;
    private final UserLessonProgressRep userLessonProgressRep;
    private final LessonRouteRep lessonRouteRep;
    private final QuizAttemptsRep quizAttemptsRep;
    private final ProgressSer progressSer;

    // Get All Lesson
    public List<AdminLessonActivityRes> getLessonActivity() {
        List<LessonRoute> lessonRoutes = lessonRouteRep.findAll();
        List<AdminLessonActivityRes> res = new ArrayList<>();
        for (LessonRoute lesson : lessonRoutes) {
            AdminLessonActivityRes alar = new AdminLessonActivityRes();
            List<UserLessonProgress> lessonProgresses = userLessonProgressRep.findByLessonRoute_Id(lesson.getId());
            alar.setLessonId(lesson.getId());
            alar.setLessonName(lesson.getName());
            alar.setTotalUser(lessonProgresses.size());
            int totalUserStarted = lessonProgresses.stream().filter(l -> !l.isLearned()).toList().size();
            alar.setTotalUserStarted(totalUserStarted);
            int totalUserCompleted = lessonProgresses.stream().filter(UserLessonProgress::isLearned).toList().size();
            alar.setTotalUserCompleted(totalUserCompleted);

            res.add(alar);
        }
        return res;
    }

    // Get Total User Level
    public List<AdminUserLevelActivityRes> getUserLevelActivity() {
        List<AdminUserLevelActivityRes> res = new ArrayList<>();
        List<UserLevel> userLevelList = userLevelRep.findAll();
        UserLevelEnum[] levels = UserLevelEnum.values();
        for (UserLevelEnum level : levels) {
            AdminUserLevelActivityRes activity = new AdminUserLevelActivityRes();
            List<UserLevel> uls = userLevelList.stream().filter(l -> l.getLevel().equals(level)).toList();
            switch (level) {
                case NOVICE: activity.setLevelName("Mới bắt đầu"); break;
                case BEGINNER: activity.setLevelName("Cơ bản"); break;
                case INTERMEDIATE: activity.setLevelName("Trung cấp"); break;
                default: activity.setLevelName("Nâng cao"); break;
            }
            activity.setTotalUser(uls.size());
            res.add(activity);
        }
        return res;
    }

    // Get Hard Char
    public List<LowestCharScoreRes> getLowestScoreCharacters() {
        return practiceRep.getLowestScoreCharacters(5);
    }

    // Get Daily Total Practice
    public List<AdminActivityDailyRes> getAdminActivityDaily(int days) {
        List<DailyScoreDateRes> dbResults = practiceRep.getSystemDailyPracticeStats(days);

        Map<String, DailyScoreDateRes> dataMap = dbResults.stream()
                .collect(Collectors.toMap(
                        res -> res.getDate().toString(),
                        res -> res,
                        (existing, replacement) -> existing
                ));

        // 3. Khởi tạo danh sách kết quả chứa đủ số ngày từ quá khứ đến hiện tại
        List<AdminActivityDailyRes> fullList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = days - 1; i >= 0; i--) {
            String targetDate = today.minusDays(i).toString();

            if (dataMap.containsKey(targetDate)) {
                DailyScoreDateRes item = dataMap.get(targetDate);
                fullList.add(new AdminActivityDailyRes(
                        targetDate,
                        item.getTotalPractice() != null ? item.getTotalPractice() : 0L
                ));
            } else {
                // Ngày không có hoạt động, gán giá trị mặc định là 0
                fullList.add(new AdminActivityDailyRes(targetDate, 0L));
            }
        }

        return fullList;
    }

    // Get User Activity
    public List<AdminActivityUseRes> getUserActivity() {
        List<Auth> auths = authRep.findAll();
        List<AdminActivityUseRes> res = new ArrayList<>();
        for (Auth auth : auths) {
            AdminActivityUseRes activity = new AdminActivityUseRes();
            // Auth
            activity.setEmail(auth.getEmail());
            activity.setActive(auth.isEnabled());
            // User
            User user = userRep.findByAuthId(auth.getId()).orElseThrow(() -> new RuntimeException("User not found: " + auth.getEmail()));
            activity.setName(user.getFullName());
            // Practice
            int countPractice = practiceRep.countByUser_Id(user.getId());
            Double avgScore = practiceRep.calculateAverageScoreByUserId(user.getId());
            activity.setTotalPractices(countPractice);
            activity.setAvgScore(avgScore != null ? avgScore : 0.0);
            // Streak
            UserStreak userStreak = userStreakRep.findByUser_Id(user.getId());
            activity.setStreak(userStreak != null ? userStreak.getCurrentStreak() : 0);

            // Add
            res.add(activity);
        }
        return res;
    }

    public AdminActivityUseRes getUserActivity(String email) {
        Auth auth = authRep.findByEmail(email).orElseThrow(() -> new RuntimeException("Auth not found: " + email));
        AdminActivityUseRes activity = new AdminActivityUseRes();
        // Auth
        activity.setEmail(auth.getEmail());
        activity.setActive(auth.isEnabled());
        // User
        User user = userRep.findByAuthId(auth.getId()).orElseThrow(() -> new RuntimeException("User not found: " + auth.getId()));
        activity.setName(user.getFullName());
        // Practice
        int countPractice = practiceRep.countByUser_Id(user.getId());
        Double avgScore = practiceRep.calculateAverageScoreByUserId(user.getId());
        activity.setTotalPractices(countPractice);
        activity.setAvgScore(avgScore != null ? avgScore : 0.0);;
        // Streak
        UserStreak userStreak = userStreakRep.findByUser_Id(user.getId());
        if (userStreak == null) {
            activity.setStreak(0);
            activity.setLastActivityDate(null);
            activity.setAttendance(null);
        } else {
            activity.setStreak(userStreak.getCurrentStreak());
            activity.setLastActivityDate(userStreak.getLastCheckInDate());
            DailyScoreReq dailyScoreReq = new DailyScoreReq();
            dailyScoreReq.setUserId(user.getId());
            dailyScoreReq.setDays(7);
            List<DailyScoreDateRes> dailyScoreDateRes = progressSer.getDaily(dailyScoreReq);
            activity.setAttendance(dailyScoreDateRes);
        }
        // Lesson Progress
        List<AdminActivityUserLessonRes> lessonUserResList = this.detailLessonsUser(user.getId());
        activity.setLearningProgress(lessonUserResList);
        // Master Character
        List<MasterRes> masterRes = progressSer.handleMasterList(user.getId());
        activity.setHandwriting(masterRes);
        // Level
        UserLevelRes level = progressSer.getUserLevel(user.getId());
        activity.setLevel(level);

        return activity;
    }

    // Get Detail Lesson User
    private List<AdminActivityUserLessonRes> detailLessonsUser(Long userId) {
        User user = userRep.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        List<UserLessonProgress> progresses = userLessonProgressRep.findByUser_Id(userId);
        List<AdminLessonUserRes> lessonUserResList = new ArrayList<>();
        int completedLessons = 0;
        int learningLessons = 0;
        for (UserLessonProgress progress : progresses) {
            Long lessonId = progress.getLessonRoute().getId();
            LessonRoute lesson = lessonRouteRep.findById(lessonId).orElseThrow(() -> new RuntimeException("Lesson not found: " + lessonId));

            AdminLessonUserRes activity = new AdminLessonUserRes();
            activity.setLessonId(lessonId);
            activity.setName(lesson.getName());
            activity.setStatus(progress.isLearned());
            if (progress.isLearned()) {
                activity.setProgress(100);
                completedLessons++;
            } else {
                activity.setProgress(0);
                learningLessons++;
            }

            AdminQuizActivityRes quizActivity = new AdminQuizActivityRes();
            Quiz quiz = lesson.getQuiz();
            if (quiz == null) {
                activity.setQuiz(null);
            } else {
                Optional<QuizAttempts> quizAttempts = quizAttemptsRep.findFirstByUser_IdAndQuiz_IdOrderByIdDesc(userId, quiz.getId());
                if (quizAttempts.isPresent()) {
                    quizActivity.setScore(quizAttempts.get().getScore());
                    quizActivity.setTotalQuestions(quizAttempts.get().getTotalQuestions());
                }
                activity.setQuiz(quizActivity);
            }
            // Add
            lessonUserResList.add(activity);
        }
        List<AdminActivityUserLessonRes> res = new ArrayList<>();
        AdminActivityUserLessonRes activity = new AdminActivityUserLessonRes();
        double totalPractice = completedLessons == 0 ? 0 : (double) completedLessons / progresses.size() * 100;
        activity.setTotalProgress(totalPractice);
        activity.setCompletedLessons(completedLessons);
        activity.setLearningLessons(learningLessons);
        activity.setLessons(lessonUserResList);
        res.add(activity);
        return res;
    }
}