package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class UserChangeProfileReq {
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private Boolean isActive;
}