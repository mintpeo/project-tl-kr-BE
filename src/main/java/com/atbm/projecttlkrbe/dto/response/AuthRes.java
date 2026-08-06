package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class AuthRes {
    private long id;
    private String email;
    private String role;
    private boolean isGoogle;
    private String fullName;
    private String numberPhone;
}
