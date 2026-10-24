package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.ListQuestionsReq;
import com.atbm.projecttlkrbe.dto.request.QuestionReq;
import com.atbm.projecttlkrbe.dto.request.QuizAttemptsReq;
import com.atbm.projecttlkrbe.dto.request.UserQuizAttemptsReq;
import com.atbm.projecttlkrbe.dto.response.GetQuestionCorrectRes;
import com.atbm.projecttlkrbe.dto.response.QuestionCorrectLevelExamRes;
import com.atbm.projecttlkrbe.dto.response.QuizRes;
import com.atbm.projecttlkrbe.dto.response.UserQuizAttemptsRes;
import com.atbm.projecttlkrbe.service.QuizSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class QuizCon {
    private final QuizSer ser;

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody Map<String, Long> body) {
        Long quizId = body.get("quizId");
        return ser.deleteQuiz(quizId);
    }

    @PostMapping("/add-new-quiz")
    public boolean addNewQuiz(@RequestBody Map<String, String> body) {
        String nameQuiz = body.get("nameQuiz");
        return ser.addNewQuiz(nameQuiz);
    }

    @PostMapping("/level-exam")
    public QuestionCorrectLevelExamRes levelExam(@RequestBody ListQuestionsReq req) {
        return ser.handleListCorrect(req);
    }

    @PostMapping("/get-quiz")
    public UserQuizAttemptsRes checkUserQuizAttempts(@RequestBody UserQuizAttemptsReq req) {
        return ser.checkUserQuizAttempts(req);
    }

    @PostMapping("/save-quiz")
    public boolean saveQuiz(@RequestBody QuizAttemptsReq req) {
        return ser.saveQuizAttempts(req);
    }

    @PostMapping("/is-correct")
    public GetQuestionCorrectRes isCorrect(@RequestBody QuestionReq req) {
        return ser.handleCorrect(req);
    }

    @PostMapping("/get-quiz-id")
    public QuizRes getQuiz(@RequestBody Map<String, Long> body) {
        long quizId = body.get("quizId");
        return ser.getQuizById(quizId);
    }
}
