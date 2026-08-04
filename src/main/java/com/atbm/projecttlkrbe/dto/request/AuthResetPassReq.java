package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class AuthResetPassReq {
    private String email;
    private String password;
}
