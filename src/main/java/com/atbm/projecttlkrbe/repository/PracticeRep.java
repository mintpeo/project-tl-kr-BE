package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.dto.response.DailyScoreDateRes;
import com.atbm.projecttlkrbe.model.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PracticeRep extends JpaRepository<Practice, Long> {
    // AVG -> No Data -> Null
    @Query("SELECT AVG(p.score) FROM Practice p WHERE p.user.id = :userId")
    Double calculateAverageScoreByUserId(Long userId);

    int countByUser_Id(Long userId);
    int countByUser_IdAndIsPassedTrue(Long userId);

    @Query(value = """
    SELECT
        DATE(p.created_at) AS date,
        ROUND(AVG(p.score), 1) AS averageScore,
        COUNT(p.id) AS totalPractice
    FROM practices p
    WHERE p.user_id = :userId
      AND p.created_at >= (CURRENT_DATE - INTERVAL :days DAY)
    GROUP BY DATE(p.created_at)
    ORDER BY date ASC
""", nativeQuery = true)
    List<DailyScoreDateRes> getDailyAccuracySince(Long userId, int days);

    List<Practice> findByUser_Id(Long userId);
    List<Practice> findByUserIdOrderByCreatedAtDesc(Long userId);
}
