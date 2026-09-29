package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.QuizAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptsRep extends JpaRepository<QuizAttempts, Long> {
    Optional<QuizAttempts> findFirstByUser_IdAndQuiz_IdOrderByScoreDesc(long userId, long quizId);
    Optional<QuizAttempts> findFirstByUser_IdAndQuiz_IdOrderByIdDesc(long userId, long quizId);
    Optional<QuizAttempts> findFirstByUserIdAndQuizIdOrderByIdDesc(Long userId, Long quizId);
    // Get List Only One
    @Query("SELECT DISTINCT q.quiz.id FROM QuizAttempts q WHERE q.user.id = :userId")
    List<Long> findDistinctQuizIdsByUserId(Long userId);
}