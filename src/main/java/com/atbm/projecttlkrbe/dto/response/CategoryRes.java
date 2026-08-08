package com.atbm.projecttlkrbe.dto.response;

import lombok.Data;

@Data
public class CategoryRes {
    private long id;
    private int orderIndex;
    private String categoryName;
    private int lessonsLength;
}
