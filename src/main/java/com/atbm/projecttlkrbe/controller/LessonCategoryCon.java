package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.response.CategoryRes;
import com.atbm.projecttlkrbe.dto.response.LessonCategoryRes;
import com.atbm.projecttlkrbe.model.LessonCategory;
import com.atbm.projecttlkrbe.service.LessonCategorySer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lesson-cate")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class LessonCategoryCon {
    private final LessonCategorySer ser;

    @GetMapping("/{categoryId}")
    public LessonCategoryRes getLessonCategory(@PathVariable Long categoryId) {
        return ser.findLessonWithCategory(categoryId);
    }

    @GetMapping("/all")
    public List<CategoryRes> getAllCate() {
        return ser.getAllCate();
    }

    @GetMapping("/get-categories")
    public List<LessonCategoryRes> getListLessonCategory() {
        return ser.getListLessonCategory();
    }
}
