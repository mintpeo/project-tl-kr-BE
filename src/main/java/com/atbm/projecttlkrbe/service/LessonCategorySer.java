package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.response.LessonCategoryRes;
import com.atbm.projecttlkrbe.dto.response.LessonRes;
import com.atbm.projecttlkrbe.model.Lesson;
import com.atbm.projecttlkrbe.model.LessonCategory;
import com.atbm.projecttlkrbe.model.LessonCategoryRel;
import com.atbm.projecttlkrbe.repository.LessonCategoryRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonCategorySer {
    private final LessonCategoryRep rep;

    // Get Lesson With Category
    public LessonCategoryRes findLessonWithCategory(long categoryId) {
        LessonCategory lc = rep.findById(categoryId).orElseThrow(() -> new RuntimeException("Lesson Category Id Not Found: " + categoryId));

        // List Lesson
        List<LessonRes> lessonResList = new ArrayList<>();
        for (LessonCategoryRel lcr : lc.getLessonCategoryRels()) {
            Lesson lesson = lcr.getLesson();

            LessonRes lessonRes = new LessonRes();
            lessonRes.setId(lesson.getId());
            lessonRes.setOrderIndex(lesson.getOrderIndex());
            lessonRes.setTitle(lesson.getTitle());
            lessonRes.setThumbnail(lesson.getThumbnail());

            lessonResList.add(lessonRes);
        }

        LessonCategoryRes res = new LessonCategoryRes();
        res.setLessons(lessonResList);
        res.setId(lc.getId());
        res.setOrderIndex(lc.getOrderIndex());
        res.setCategoryName(lc.getName());

        return res;
    }

    // Get List Category
    public List<LessonCategory> getAllCate() {
        return rep.findAll();
    }

    // Get List Category Custom
    public List<LessonCategoryRes> getListLessonCategory() {
        List<LessonCategory> lessonCategories = rep.findAll();

        List<LessonCategoryRes> result = new ArrayList<>();
        for (LessonCategory lc : lessonCategories) {
            LessonCategoryRes res = new LessonCategoryRes();
            res.setId(lc.getId());
            res.setOrderIndex(lc.getOrderIndex());
            res.setCategoryName(lc.getName());

            result.add(res);
        }

        return result;
    }
}
