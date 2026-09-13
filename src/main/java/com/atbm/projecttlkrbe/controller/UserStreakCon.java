package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.model.UserStreak;
import com.atbm.projecttlkrbe.service.UserStreakSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user-streak")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class UserStreakCon {
    private final UserStreakSer ser;

    @PostMapping("/get")
    public UserStreak getUserStreak(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        return ser.getUserStreak(userId);
    }

    @PostMapping("/check-in")
    public void checkIn(@RequestBody Map<String, Long> body) {
        long userId = body.get("userId");
        ser.checkIn(userId);
    }
}
