package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class AuthChangePassReq {
    private String email;
    private String oldPassword;
    private String newPassword;
}
