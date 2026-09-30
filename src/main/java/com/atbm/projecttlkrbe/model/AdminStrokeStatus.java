package com.atbm.projecttlkrbe.model;

public enum AdminStrokeStatus {
    VERIFIED("Đã duyệt"),
    PENDING("Chờ duyệt"),
    FLAGGED("Cần chỉnh sửa"),
    MISSING("Thiếu dữ liệu");

    private final String displayName;

    AdminStrokeStatus(String displayName) {
        this.displayName = displayName;
    }
}
