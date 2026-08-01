package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class VerifyEmailReq {
    private String email;
    private String code;
}
