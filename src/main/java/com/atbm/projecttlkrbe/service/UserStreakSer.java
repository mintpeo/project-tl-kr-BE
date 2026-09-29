package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.UserStreakRes;
import com.atbm.projecttlkrbe.model.User;
import com.atbm.projecttlkrbe.model.UserStreak;
import com.atbm.projecttlkrbe.repository.UserRep;
import com.atbm.projecttlkrbe.repository.UserStreakRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserStreakSer {
    private final UserStreakRep rep;
    private final UserRep userRep;

    // Get Streak
    public UserStreakRes getUserStreak(Long userId) {
        User user = userRep.findById(userId).orElseThrow(() -> new RuntimeException("user not found: " + userId));
        UserStreak userStreak = rep.findByUser_Id(user.getId());
        UserStreakRes res = new UserStreakRes();
        if (userStreak == null) {
            res.setCurrentStreak(0);
            res.setLongestStreak(0);
            res.setLastCheckInDate(null);
            res.setChecked(false);
            return res;
        }

        res.setCurrentStreak(userStreak.getCurrentStreak());
        res.setLongestStreak(userStreak.getLongestStreak());
        res.setLastCheckInDate(userStreak.getLastCheckInDate());
        if (userStreak.getLastCheckInDate().equals(LocalDate.now())) res.setChecked(true);
        return res;
    }

    // Check in
    public boolean checkIn(long userId) {
        User user = userRep.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));
        // Local Date -> Not save Time
        LocalDate now = LocalDate.now();

        UserStreak userStreak = rep.findByUser_Id(user.getId());
        if (userStreak == null) {
            // Never check in before -> create
            UserStreak create = new UserStreak();
            create.setUser(user);
            create.setCurrentStreak(1);
            create.setLongestStreak(1);
            create.setLastCheckInDate(now);
            rep.save(create);
            return true;
        }

        LocalDate lastCheckinDate = userStreak.getLastCheckInDate();
        long daysBetween = ChronoUnit.DAYS.between(lastCheckinDate, now);

        // Checked in to day
        if (daysBetween == 0) {
            throw new RuntimeException("Bạn đã điểm danh ngày hôm nay!");
        }
        // Check in today
        else if (daysBetween == 1) userStreak.setCurrentStreak(userStreak.getCurrentStreak() + 1);
        // Not take attendance consecutively (Lose streak)
        else userStreak.setCurrentStreak(1);

        // Update longest streak
        if (userStreak.getCurrentStreak() > userStreak.getLongestStreak()) {
            userStreak.setLongestStreak(userStreak.getCurrentStreak());
        }

        userStreak.setLastCheckInDate(now);
        rep.save(userStreak);
        return true;
    }
}
