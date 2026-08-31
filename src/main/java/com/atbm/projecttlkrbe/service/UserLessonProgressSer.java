package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.CompleteLessonReq;
import com.atbm.projecttlkrbe.model.LessonRoute;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.model.UserLessonProgress;
import com.atbm.projecttlkrbe.repository.LessonRouteRep;
import com.atbm.projecttlkrbe.repository.UserLessonProgressRep;
import com.atbm.projecttlkrbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLessonProgressSer {
    private final UserLessonProgressRep rep;
    private final UserRep userRep;
    private final LessonRouteRep lessonRoute;

    // Finish Lesson
    public boolean setCompleteLesson(CompleteLessonReq req) {
        long userId = req.getUserId();
        long lessonId = req.getLessonId();

        UserLessonProgress userLessonProgress = rep.findByUser_IdAndLessonRoute_Id(userId, lessonId).orElseThrow(() -> new RuntimeException("User lesson progress not found: " + userId));
        userLessonProgress.setLearned(true);
        userLessonProgress.setCompletedAt(LocalDateTime.now());
        rep.save(userLessonProgress);
        return true;
    }

    // Create Road Lesson
    public boolean createUserLessonProgress(long userId) {
        List<LessonRoute> lessonRoutes = lessonRoute.findAll();
        User user = userRep.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));
        for (LessonRoute lr : lessonRoutes) {
            if (rep.existsByUser_IdAndLessonRoute_Id(userId, lr.getId())) continue;

            UserLessonProgress userLessonProgress = new UserLessonProgress();
            userLessonProgress.setUser(user);
            userLessonProgress.setLessonRoute(lr);
            rep.save(userLessonProgress);
        }
        return true;
    }
}
