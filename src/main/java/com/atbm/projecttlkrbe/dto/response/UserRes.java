package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class UserRes {
    private long id;
    private long userId;
    private String email;
    private String role;
    private boolean isGoogle;
    private String fullName;
    private String numberPhone;
    private boolean isLessonRoad;
}
