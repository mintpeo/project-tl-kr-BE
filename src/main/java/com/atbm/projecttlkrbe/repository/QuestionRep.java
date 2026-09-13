package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRep extends JpaRepository<Question, Long> {
    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.options WHERE q.quiz.id = :quizId")
    List<Question> findByQuizIdWithOptions(long quizId);
    List<Question> findByQuizId(long quizId);
}
