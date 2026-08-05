package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.service.UserSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class UserCon {
    private final UserSer ser;


}
