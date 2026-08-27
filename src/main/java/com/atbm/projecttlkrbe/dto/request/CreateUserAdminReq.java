package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

@Data
public class CreateUserAdminReq {
    private String fullName;
    private String email;
    private String password;
    private String numberPhone;
    private boolean active;
    private String role;
}
