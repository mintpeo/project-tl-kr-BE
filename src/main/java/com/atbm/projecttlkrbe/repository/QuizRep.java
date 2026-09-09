package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRep extends JpaRepository<Quiz, Long> {
}
