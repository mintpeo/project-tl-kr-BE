package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.response.UserLevelRes;
import com.atbm.projecttlkrbe.service.UserLevelSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user-level")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class UserLevelCon {
    private final UserLevelSer ser;

    @PostMapping("/get")
    public UserLevelRes getUserLevelByUserId(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        return ser.getUserLevelByUserId(userId);
    }
}
