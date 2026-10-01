package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.AdminActivityDailyRes;
import com.atbm.projecttlkrbe.dto.response.AdminActivityUseRes;
import com.atbm.projecttlkrbe.dto.response.DailyScoreDateRes;
import com.atbm.projecttlkrbe.dto.response.LowestCharScoreRes;
import com.atbm.projecttlkrbe.model.Auth;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.model.UserStreak;
import com.atbm.projecttlkrbe.repository.AuthRep;
import com.atbm.projecttlkrbe.repository.PracticeRep;
import com.atbm.projecttlkrbe.repository.UserRep;
import com.atbm.projecttlkrbe.repository.UserStreakRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminActivitySer {
    private final AuthRep authRep;
    private final UserRep userRep;
    private final PracticeRep practiceRep;
    private final UserStreakRep userStreakRep;

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
}