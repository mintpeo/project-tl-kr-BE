package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.AddLessonRouteReq;
import com.atbm.projecttlkrbe.dto.request.AdminLessonOrderIndexReq;
import com.atbm.projecttlkrbe.dto.request.EditLessonRouteReq;
import com.atbm.projecttlkrbe.dto.response.LessonCategoryRouteRes;
import com.atbm.projecttlkrbe.model.LessonCategoryRoute;
import com.atbm.projecttlkrbe.model.LessonContent;
import com.atbm.projecttlkrbe.model.LessonRoute;
import com.atbm.projecttlkrbe.model.UserLessonProgress;
import com.atbm.projecttlkrbe.repository.LessonCategoryRouteRep;
import com.atbm.projecttlkrbe.repository.LessonContentRep;
import com.atbm.projecttlkrbe.repository.LessonRouteRep;
import com.atbm.projecttlkrbe.repository.UserLessonProgressRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminLessonSer {
    private final LessonRouteRep lessonRouteRep;
    private final LessonCategoryRouteRep lessonCategoryRouteRep;
    private final LessonCategoryRouteSer lessonCategoryRouteSer;
    private final UserLessonProgressRep userLessonProgressRep;
    private final LessonContentRep lessonContentRep;

    // Update Order Index
    public boolean updateOrderIndex(AdminLessonOrderIndexReq req) {
        List<Long> lessonsId = req.getLessonsId();
        if (lessonsId == null || lessonsId.isEmpty()) return false;

        List<LessonRoute> lessons = lessonRouteRep.findByCateRouteId(req.getCateRouteId());
        Map<Long, LessonRoute> lessonMap = lessons.stream()
                .collect(Collectors.toMap(LessonRoute::getId, Function.identity()));

        for (int i = 0; i < lessonsId.size(); i++) {
            Long lessonId = lessonsId.get(i);
            int newOrderIndex = i + 1;

            LessonRoute lesson = lessonMap.get(lessonId);
            if (lesson == null) throw new RuntimeException("Bài học không thuộc danh mục này: " + lessonId);

            lesson.setOrderIndex(newOrderIndex);
        }

        lessonRouteRep.saveAll(lessons);
        return true;
    }

    // Delete Lesson
    public boolean deleteLesson(long lessonId) {
        LessonRoute lessonRoute = lessonRouteRep.findById(lessonId).orElseThrow(() -> new RuntimeException("LessonRoute not found: " + lessonId));
        List<UserLessonProgress> userLessonProgresses = userLessonProgressRep.findByLessonRoute_Id(lessonId);

        for (UserLessonProgress ulp : userLessonProgresses) {
            userLessonProgressRep.delete(ulp);
        }

        List<LessonContent> lessonContents = lessonContentRep.findAllByLessonRoute_Id(lessonId);
        for (LessonContent lc : lessonContents) {
            lessonContentRep.delete(lc);
        }

        lessonRouteRep.delete(lessonRoute);
        return true;
    }

    // Add Lesson
    public boolean addLessonRoute(AddLessonRouteReq req) {
        String name = req.getName();
        long cateRouteId = req.getCateRouteId();
        boolean isActive = req.isActive();
        String duration = req.getDuration();
        String description = req.getDescription();
        String youtubeId = req.getYoutubeId();

        LessonRoute lesson = new LessonRoute();
        lesson.setName(name);

        Integer maxOrderIndex = lessonRouteRep.findMaxOrderIndexByCateRouteId(req.getCateRouteId());
        // Chua co bai hoc nao
        int nextOrderIndex = (maxOrderIndex != null) ? maxOrderIndex + 1 : 1;
        lesson.setOrderIndex(nextOrderIndex);

        lesson.setActive(isActive);
        lesson.setDuration(duration);
        lesson.setDescription(description);
        lesson.setYoutubeId(youtubeId);
        lesson.setCreatedAt(LocalDateTime.now());

        LessonCategoryRoute lessonCate = lessonCategoryRouteRep.findById(cateRouteId).orElseThrow(() -> new RuntimeException("LessonCategoryRoute not found: " + cateRouteId));
        lesson.setCateRoute(lessonCate);

        lessonRouteRep.save(lesson);
        return true;
    }

    // Edit Lesson
    public boolean editLessonRoute(EditLessonRouteReq req) {
        long lessonId = req.getId();
        String name = req.getName();
        Long cateRouteId = req.getCateRouteId();
        int orderIndex = req.getOrderIndex();
        Boolean isActive = req.getIsActive();
        String duration = req.getDuration();
        String description = req.getDescription();
        String youtubeId = req.getYoutubeId();

        LessonRoute lesson = lessonRouteRep.findById(lessonId).orElseThrow(() -> new RuntimeException("LessonRoute not found: " + lessonId));
        if (name != null && !name.trim().isEmpty()) lesson.setName(name);
        if (cateRouteId != null && (lesson.getCateRoute() == null || !cateRouteId.equals(lesson.getCateRoute().getId()))) {
            LessonCategoryRoute lessonCate = lessonCategoryRouteRep.findById(cateRouteId).orElseThrow(() -> new RuntimeException("LessonCategoryRoute not found: " + cateRouteId));
            lesson.setCateRoute(lessonCate);
        }
        lesson.setOrderIndex(orderIndex);
        if (isActive != null && isActive != lesson.isActive()) lesson.setActive(isActive);
        if (duration != null && !duration.trim().isEmpty()) lesson.setDuration(duration);
        if (description != null && !description.trim().isEmpty()) lesson.setDescription(description);
        if (youtubeId != null && !youtubeId.trim().isEmpty()) lesson.setYoutubeId(youtubeId);
        lesson.setUpdatedAt(LocalDateTime.now());
        lessonRouteRep.save(lesson);
        return true;
    }

    // Get Category Road
    public List<LessonCategoryRouteRes> getCateRoute() {
        return lessonCategoryRouteSer.getCateRoute();
    }

    // Search Lesson By Name
    public List<LessonRoute> getLessonByName(String lessonRoadName) {
        return lessonRouteRep.findByNameContainingIgnoreCase(lessonRoadName);
    }

    // Get Lesson Road
    public List<LessonRoute> getAllLessonRoutes() {
        return lessonRouteRep.findAll();
    }
}
