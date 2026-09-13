package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.DailyScoreReq;
import com.atbm.projecttlkrbe.dto.response.*;
import com.atbm.projecttlkrbe.model.CharacterEntity;
import com.atbm.projecttlkrbe.model.LessonCategoryRoute;
import com.atbm.projecttlkrbe.model.Practice;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressSer {
    private final PracticeRep practiceRep;
    private final UserRep userRep;
    private final CharacterRep characterRep;
    private final LessonCategoryRouteSer lessonCategoryRouteSer;
    private final LessonRouteSer lessonRouteSer;

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
