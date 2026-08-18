package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.AuthChangePassReq;
import com.atbm.projecttlkrbe.dto.request.UserChangeProfileReq;
import com.atbm.projecttlkrbe.dto.response.AuthRes;
import com.atbm.projecttlkrbe.dto.response.UserRes;
import com.atbm.projecttlkrbe.service.UserSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class UserCon {
    private final UserSer ser;

    @PostMapping("/me")
    public UserRes me(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return ser.getInfoUser(email);
    }

    @PatchMapping("/change-pass-profile")
    public UserRes changePassword(@RequestBody AuthChangePassReq req) {
        return ser.changePassword(req);
    }

    @PatchMapping("/change-profile")
    public boolean changeProfile(@RequestBody UserChangeProfileReq req) {
        return ser.changeProfile(req);
    }
}
