package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.*;
import com.atbm.projecttlkrbe.dto.response.*;
import com.atbm.projecttlkrbe.model.*;
import com.atbm.projecttlkrbe.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class AdminCon {
    private final AdminSer ser;
    private final AdminLessonSer lessonSer;
    private final AdminCharacterSer characterSer;
    private final AdminExerciseSer exerciseSer;
    private final AdminActivitySer activitySer;
    private final AdminStrokeDataSer strokeDataSer;

    @GetMapping("/all-option-stroke")
    public List<StrokeOptionData> getAllOptions() {
        return strokeDataSer.getAllOptions();
    }

    @GetMapping("/all-stroke")
    public List<AdminStrokeDataRes> getAllStroke() {
        return strokeDataSer.getAllStroke();
    }

    @PutMapping("/upload-active")
    public boolean approveDaft(@RequestBody Map<String, Long> body) throws IOException {
        Long charId = body.get("charId");
        return strokeDataSer.approveDaft(charId);
    }

    @PutMapping("/upload-pending")
    public boolean uploadPending(@RequestParam("charId") Long charId, @RequestParam("file") MultipartFile file) throws IOException {
        return strokeDataSer.uploadDraft(charId, file);
    }

    @GetMapping("/hard-char")
    public List<LowestCharScoreRes> getLowestCharScores() {
        return activitySer.getLowestScoreCharacters();
    }

    @PostMapping("/daily-activity")
    public List<AdminActivityDailyRes> getDailyActivity(@RequestBody Map<String, Integer> body) {
        int days = body.get("days");
        return activitySer.getAdminActivityDaily(days);
    }

    @GetMapping("/get-user-activity")
    public List<AdminActivityUseRes> getUserActivity() {
        return activitySer.getUserActivity();
    }

    @DeleteMapping("/delete-question")
    public boolean deleteQuestion(@RequestBody Map<String, Long> body) {
        Long questionId = body.get("questionId");
        return exerciseSer.deleteQuestion(questionId);
    }

    @PostMapping("/create-question")
    public boolean createQuestion(@RequestBody AdminCreateQuestionReq req) {
        return exerciseSer.createQuestion(req);
    }

    @PatchMapping("/update-question")
    public boolean updateQuestion(@RequestBody AdminUpdateQuestionReq req) {
        return exerciseSer.updateQuestion(req);
    }

    @GetMapping("/get-quizzes")
    public List<AdminQuizRes> getQuizzes() {
        return exerciseSer.getQuizzes();
    }

    @GetMapping("/get-questions")
    public List<AdminQuestionRes> getQuestions() {
        return exerciseSer.getQuestions();
    }

    @PutMapping("/upload-char")
    public ResponseEntity<String> uploadCharacter(@ModelAttribute UploadFileCharacterReq req, @RequestParam("file") MultipartFile file) {
        return characterSer.uploadFile(req, file);
    }

    @PostMapping("/update-order-index")
    public boolean updateOrderIndex(@RequestBody AdminLessonOrderIndexReq req) {
        return lessonSer.updateOrderIndex(req);
    }

    @PostMapping("/edit-char")
    public boolean editChar(@RequestBody EditCharReq req) {
        return characterSer.editChar(req);
    }

    @PostMapping("/search-char")
    public List<CharacterEntity> searchCharacters(@RequestBody Map<String ,String> body) {
        String keyword = body.get("keyword");
        return characterSer.searchCharacters(keyword);
    }

    @GetMapping("/all-char")
    public List<CharacterRes> getAllCharacters() {
        return characterSer.getAllCharacters();
    }

    @DeleteMapping("/delete-lesson")
    public boolean deleteLesson(@RequestBody Map<String, Long> body) {
        Long lessonId = body.get("lessonId");
        return lessonSer.deleteLesson(lessonId);
    }

    @PostMapping("/add-lesson")
    public boolean addLesson(@RequestBody AddLessonRouteReq req) {
        return lessonSer.addLessonRoute(req);
    }

    @PostMapping("/edit-lesson")
    public boolean editLesson(@RequestBody EditLessonRouteReq req) {
        return lessonSer.editLessonRoute(req);
    }

    @GetMapping("/all-cate-road")
    public List<LessonCategoryRouteRes> getCateRoute() {
        return lessonSer.getCateRoute();
    }

    @PostMapping("/search-lessons-name")
    public List<LessonRoute> getLessonsByName(@RequestBody Map<String, String> body) {
        String name = body.get("lessonRoadName");
        return lessonSer.getLessonByName(name);
    }

    @GetMapping("/all-lessons-road")
    public List<LessonRoute> getAllLessonRoutes() {
        return lessonSer.getAllLessonRoutes();
    }

    @DeleteMapping("/delete")
    public boolean deleteUser(@RequestBody Map<String, Long> body) {
        long authId = body.get("authId");
        return ser.deleteUser(authId);
    }

    @PostMapping("/create")
    public boolean createUser(@RequestBody CreateUserAdminReq req) {
        return ser.createUser(req);
    }

    @PatchMapping("/change")
    public boolean changeUser(@RequestBody UserChangeProfileReq req) {
        return ser.changeProfileUser(req);
    }

    @PostMapping("/email")
    public List<AdminUserRes> getAdminUserByEmail(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return ser.getAdminUsersByEmail(email);
    }

    @GetMapping("/all")
    public List<AdminUserRes> getAllAuth() {
        return ser.getAllAuth();
    }
}