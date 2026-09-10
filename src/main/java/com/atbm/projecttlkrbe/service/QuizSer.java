package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.QuestionReq;
import com.atbm.projecttlkrbe.dto.request.QuizAttemptsReq;
import com.atbm.projecttlkrbe.dto.request.UserQuizAttemptsReq;
import com.atbm.projecttlkrbe.dto.response.*;
import com.atbm.projecttlkrbe.model.*;
import com.atbm.projecttlkrbe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizSer {
    private final QuizRep rep;
    private final QuestionRep questionRep;
    private final OptionRep optionRep;
    private final QuizAttemptsRep quizAttemptsRep;
    private final UserRep userRep;

    // Check User Have Quiz Attempts
    public UserQuizAttemptsRes checkUserQuizAttempts(UserQuizAttemptsReq req) {
        UserQuizAttemptsRes res = new UserQuizAttemptsRes();

        Optional<QuizAttempts> quizAttempt = quizAttemptsRep.findFirstByUser_IdAndQuiz_IdOrderByScoreDesc(req.getUserId(), req.getQuizId());
        if (quizAttempt.isEmpty()) {
            res.setQuizAttempt(false);
            return res;
        }

        res.setQuizAttempt(true);
        res.setScore(quizAttempt.get().getScore());

        // Last Near
        Optional<QuizAttempts> lastQuiz = quizAttemptsRep.findFirstByUser_IdAndQuiz_IdOrderByIdDesc(req.getUserId(), req.getQuizId());
        res.setLastDate(lastQuiz.get().getCompletedDate());
        res.setLastScore(lastQuiz.get().getScore());

        return res;
    }

    // Save Quiz Attempts
    public boolean saveQuizAttempts(QuizAttemptsReq req) {
        QuizAttempts res = new QuizAttempts();

        User user = userRep.findById(req.getUserId()).orElseThrow(() -> new RuntimeException("User not found: " + req.getUserId()));
        res.setUser(user);
        Quiz quiz = rep.findById(req.getQuizId()).orElseThrow(() -> new RuntimeException("Quiz not found: " + req.getQuizId()));
        res.setQuiz(quiz);

        res.setScore(req.getScore());
        res.setTotalQuestions(req.getTotalQuestions());
        res.setCompletedDate(LocalDate.now());
        quizAttemptsRep.save(res);
        return true;
    }

    // Handle Correct
    public GetQuestionCorrectRes handleCorrect(QuestionReq req) {
        List<Option> options = optionRep.findByQuestion_Id(req.getQuestionId());
        Long correctAnswer = options.stream().filter(Option::isCorrect)
                .map(Option::getId).findFirst().orElseThrow(() -> new RuntimeException("Not found answer correct"));

        boolean isCorrect = optionRep.findByIdAndQuestion_Id(req.getOptionId(),  req.getQuestionId())
                .map(Option :: isCorrect)
                .orElse(false);

        // Res
        GetQuestionCorrectRes res = new GetQuestionCorrectRes();
        res.setCorrect(isCorrect);
        res.setCorrectAnswer(correctAnswer);
        return res;
    }

    // Get Quiz
    public QuizRes getQuizz(long quizzId) {
        QuizRes res = new QuizRes();
        // Quiz
        Quiz quiz = rep.findById(quizzId).orElseThrow(() -> new RuntimeException("Quiz Not Found: " + quizzId));
        res.setId(quiz.getId());
        res.setTitle(quiz.getTitle());
        res.setDescription(quiz.getDescription());

        // Question
        List<QuestionRes> questions = getQuestions(quizzId);
        res.setQuestions(questions);

        return res;
    }
    // Get Questions
    private List<QuestionRes> getQuestions(long quizzId) {
        List<Question> questions = questionRep.findByQuizIdWithOptions(quizzId);
        List<QuestionRes> res = new ArrayList<>();

        for (Question question : questions) {
            QuestionRes questionRes = new QuestionRes();
            questionRes.setId(question.getId());
            questionRes.setQuestionText(question.getQuestionText());
            questionRes.setQuestionMediaUrl(question.getMediaUrl());

            List<OptionRes> options = new ArrayList<>();
            for (Option option : question.getOptions()) {
                OptionRes optionResItem = new OptionRes();
                optionResItem.setOptionId(option.getId());
                optionResItem.setOptionText(option.getOptionText());
                options.add(optionResItem);
            }
            questionRes.setOptions(options);
            res.add(questionRes);
        }
        return res;
    }
}
