package com.atbm.projecttlkrbe.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AdminCateOrderIndexReq {
    private List<Long> cateIds;
}
