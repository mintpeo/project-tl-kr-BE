package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.AddLessonRouteReq;
import com.atbm.projecttlkrbe.dto.request.AdminCateOrderIndexReq;
import com.atbm.projecttlkrbe.dto.request.AdminLessonOrderIndexReq;
import com.atbm.projecttlkrbe.dto.request.EditLessonRouteReq;
import com.atbm.projecttlkrbe.dto.response.LessonCategoryRouteRes;
import com.atbm.projecttlkrbe.model.*;
import com.atbm.projecttlkrbe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final QuizRep quizRep;

    // Update Order Index Cate
    public boolean updateOrderIndexCate(AdminCateOrderIndexReq req) {
        List<Long> cateIds = req.getCateIds();
        List<LessonCategoryRoute> cateList = lessonCategoryRouteRep.findAll();
        for (int i = 0; i < cateIds.size(); i++) {
            Long cateId = cateIds.get(i);
            int newOrderIndex = i + 1;

            LessonCategoryRoute cate = cateList.stream()
                    .filter(c -> cateId.equals(c.getId())).findFirst()
                    .orElse(null);
            if (cate == null) return false;

            cate.setOrderIndex(newOrderIndex);
        }

        lessonCategoryRouteRep.saveAll(cateList);
        return true;
    }

    // Edit Cate
    public boolean editCate(Long cateId) {
        LessonCategoryRoute cate = lessonCategoryRouteRep.findById(cateId).orElse(null);
        if (cate == null) return false;
        boolean active = cate.isActive();
        cate.setActive(!active);
        lessonCategoryRouteRep.save(cate);
        return true;
    }

    // Delete Cate
    public boolean deleteCate(Long cateId) {
        LessonCategoryRoute cate = lessonCategoryRouteRep.findById(cateId).orElse(null);
        if (cate == null) return false;
        List<LessonRoute> lessonRoutes = lessonRouteRep.findByCateRouteId(cateId);
        for (LessonRoute lessonRoute : lessonRoutes) lessonRoute.setCateRoute(null);
        lessonRouteRep.saveAll(lessonRoutes);
        lessonCategoryRouteRep.delete(cate);
        return true;
    }

    // Create New Cate
    public boolean createNewCate(String cateName) {
        LessonCategoryRoute newCate = new LessonCategoryRoute();
        newCate.setName(cateName);
        newCate.setActive(true);
        lessonCategoryRouteRep.save(newCate);
        return true;
    }

    // Get All Cate
    public List<LessonCategoryRouteRes> getLessonCategoryRoutes() {
        List<LessonCategoryRoute> list = lessonCategoryRouteRep.findAll();
        List<LessonCategoryRouteRes> res = new ArrayList<>();
        for (LessonCategoryRoute l : list) {
            // Result
            LessonCategoryRouteRes r = new LessonCategoryRouteRes();
            long cateRouteId = l.getId();
            r.setId(cateRouteId);
            r.setName(l.getName());
            r.setOrderIndex(l.getOrderIndex());
            r.setDes(l.getDescription());
            r.setActive(l.isActive());
            // Lesson Size
            List<LessonRoute> lessons = lessonRouteRep.findByCateRouteId(cateRouteId);
            r.setLessonsSize(lessons.size());
            res.add(r);
        }
        return res;
    }

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
//        int orderIndex = req.getOrderIndex();
        Boolean isActive = req.getIsActive();
        String duration = req.getDuration();
        String description = req.getDescription();
        String youtubeId = req.getYoutubeId();
        Long quizId = req.getQuizId();

        LessonRoute lesson = lessonRouteRep.findById(lessonId).orElseThrow(() -> new RuntimeException("LessonRoute not found: " + lessonId));
        if (name != null && !name.trim().isEmpty()) lesson.setName(name);
        if (cateRouteId != null && (lesson.getCateRoute() == null || !cateRouteId.equals(lesson.getCateRoute().getId()))) {
            LessonCategoryRoute lessonCate = lessonCategoryRouteRep.findById(cateRouteId).orElseThrow(() -> new RuntimeException("LessonCategoryRoute not found: " + cateRouteId));
            lesson.setCateRoute(lessonCate);
        }
//        if (orderIndex != lesson.getOrderIndex()) lesson.setOrderIndex(orderIndex);
        if (isActive != null && isActive != lesson.isActive()) lesson.setActive(isActive);
        if (duration != null && !duration.trim().isEmpty()) lesson.setDuration(duration);
        if (description != null && !description.trim().isEmpty()) lesson.setDescription(description);
        if (youtubeId != null && !youtubeId.trim().isEmpty()) lesson.setYoutubeId(youtubeId);
        lesson.setUpdatedAt(LocalDateTime.now());

        // Quiz
        if (quizId != null && !quizId.equals(lesson.getQuiz().getId())) {
            Quiz quiz = quizRep.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found: " + quizId));
            LessonRoute oldLesson = lessonRouteRep.findByQuizId(quizId).orElse(null);
            if (oldLesson == null) lesson.setQuiz(quiz);
            else {
                oldLesson.setQuiz(null);
                lessonRouteRep.save(oldLesson);
                lesson.setQuiz(quiz);
            }
        }

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
