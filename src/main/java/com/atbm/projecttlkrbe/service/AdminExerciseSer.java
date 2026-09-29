package com.atbm.projecttlkrbe.service;

import com.atbm.projecttlkrbe.dto.request.AdminCreateOptionReq;
import com.atbm.projecttlkrbe.dto.request.AdminCreateQuestionReq;
import com.atbm.projecttlkrbe.dto.request.AdminUpdateOptionReq;
import com.atbm.projecttlkrbe.dto.request.AdminUpdateQuestionReq;
import com.atbm.projecttlkrbe.dto.response.*;
import com.atbm.projecttlkrbe.model.*;
import com.atbm.projecttlkrbe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminExerciseSer {
    private final QuizRep quizRep;
    private final QuestionRep questionRep;
    private final OptionRep optionRep;

    // Delete Question
    public boolean deleteQuestion(Long questionId) {
        Question question = questionRep.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found: " + questionId));
        List<Option> options = question.getOptions();
        optionRep.deleteAll(options);
        questionRep.delete(question);
        return true;
    }

    // Create Question
    public boolean createQuestion(AdminCreateQuestionReq req) {
        Quiz quiz = quizRep.findById(req.getQuizId()).orElseThrow(() -> new RuntimeException("Quiz Not Found: " + req.getQuizId()));
        Question res = new Question();
        // Question
        res.setQuiz(quiz);
        res.setQuestionText(req.getQuestion());
        res.setMediaUrl(req.getQuestionMediaUrl());
        Question savedQuestion = questionRep.save(res);
        // Options
        List<Option> options = new ArrayList<>();
        List<AdminCreateOptionReq> optionReqs = req.getOptions();
        for (int i = 0; i < optionReqs.size(); i++) {
            AdminCreateOptionReq optionReq = optionReqs.get(i);
            Option option = new Option();
            option.setOptionText(optionReq.getOptionText());
            option.setQuestion(savedQuestion);
            if (i == req.getCorrect()) option.setCorrect(true);
            else option.setCorrect(false);
            options.add(option);
        }
        optionRep.saveAll(options);
        return true;
    }

    // Update Question
    public boolean updateQuestion(AdminUpdateQuestionReq req) {
        // Question
        Long questionId = req.getId();
        Question question = questionRep.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found: " + questionId));
        String questionName = req.getQuestion();
        if (questionName != null && !questionName.trim().isEmpty()) question.setQuestionText(questionName);
        String questionMediaUrl = req.getQuestionMediaUrl();
        if (questionMediaUrl != null && !questionMediaUrl.trim().isEmpty()) question.setMediaUrl(questionMediaUrl);

        // Change Quiz for Question
        Long quizId = req.getQuizId();
        if (quizId != null) {
            Quiz quiz = quizRep.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found: " + quizId));
            question.setQuiz(quiz);
        }
        questionRep.save(question);
        if (req.getOptions() != null && !req.getOptions().isEmpty()) {
            return updateOption(req.getOptions(), questionId, req.getCorrect());
        }
        return true;
    }
    // Update Option
    private boolean updateOption(List<AdminUpdateOptionReq> options, Long questionId, Long optionIdCorrect) {
        if (options == null || options.isEmpty()) return false;

        List<Option> optionList = optionRep.findByQuestion_Id(questionId);
        Map<Long, Option> optionMap = optionList.stream()
                .collect(Collectors.toMap(Option::getId, o -> o));
        List<Option> updatedList = new ArrayList<>();

        for (AdminUpdateOptionReq optionReq : options) {
            Long optionId = optionReq.getId();
            Option entity = optionMap.get(optionId);
            if (entity != null) {
                String optionName = optionReq.getOptionText();
                if (optionName != null && !optionName.trim().isEmpty()) entity.setOptionText(optionName);
                if (optionId.equals(optionIdCorrect)) entity.setCorrect(true);
                else entity.setCorrect(false);
                updatedList.add(entity);
            } else throw new RuntimeException("Option not found: " + optionId);
        }
        optionRep.saveAll(updatedList);
        return true;
    }

    // Get All Quiz Name
    public List<AdminQuizRes> getQuizzes() {
        List<Quiz> quizzes = quizRep.findAll();
        List<AdminQuizRes> res = new ArrayList<>();
        for (Quiz quiz : quizzes) {
            AdminQuizRes ale = new AdminQuizRes();
            ale.setId(quiz.getId());
            ale.setName(quiz.getTitle());
            res.add(ale);
        }
        return res;
    }

    // Get All Questions
    public List<AdminQuestionRes> getQuestions() {
        List<Question> questions = questionRep.findAll();
        List<AdminQuestionRes> res = new ArrayList<>();
        for (Question question : questions) {
            AdminQuestionRes ale = new AdminQuestionRes();
            Long questionId = question.getId();
            ale.setId(questionId);
            ale.setQuestion(question.getQuestionText());
            ale.setQuestionMediaUrl(question.getMediaUrl());
            // Quiz id
            Quiz quiz = question.getQuiz();
            ale.setQuizId(quiz != null ? quiz.getId() : null);
            // Options
            List<AdminOptionRes> options = new ArrayList<>();
            for (Option option : question.getOptions()) {
                AdminOptionRes optionResItem = new AdminOptionRes();
                optionResItem.setId(option.getId());
                optionResItem.setOptionText(option.getOptionText());
                optionResItem.setCorrect(option.isCorrect());
                options.add(optionResItem);
            }
            ale.setOptions(options);
            res.add(ale);
        }
        return res;
    }
}
