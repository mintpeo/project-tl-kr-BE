package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserRes {
    private long authId;
    private String mail;
    private String role;
    private LocalDateTime authCreateAt;
    private boolean isActive;
    private boolean isGoogle;

    private long userId;
    private String fullName;
    private String phone;
}
