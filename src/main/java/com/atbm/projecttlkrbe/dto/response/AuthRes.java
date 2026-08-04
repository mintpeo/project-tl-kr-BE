package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class AuthRes {
    private long id;
    private String name;
    private String email;
    private String role;
}
