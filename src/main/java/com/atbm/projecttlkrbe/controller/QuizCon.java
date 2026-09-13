package com.atbm.projecttlkrbe.controller;

import com.atbm.projecttlkrbe.dto.request.QuestionReq;
import com.atbm.projecttlkrbe.dto.request.QuizAttemptsReq;
import com.atbm.projecttlkrbe.dto.request.UserQuizAttemptsReq;
import com.atbm.projecttlkrbe.dto.response.GetQuestionCorrectRes;
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

    @PostMapping("/all")
    public QuizRes getQuiz(@RequestBody Map<String, Long> body) {
        long quizId = body.get("quizId");
        return ser.getQuizz(quizId);
    }
}
