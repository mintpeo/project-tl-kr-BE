package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.dto.response.CharAverageScoreRes;
import com.atbm.projecttlkrbe.dto.response.DailyScoreDateRes;
import com.atbm.projecttlkrbe.dto.response.LowestCharScoreRes;
import com.atbm.projecttlkrbe.model.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    // Mastery Char
    @Query(value = """
        SELECT 
            p.character_id AS characterId,
            ROUND(AVG(p.score), 1) AS averageScore,
            COUNT(p.id) AS totalAttempts
        FROM practices p
        WHERE p.user_id = :userId
          AND p.character_id IS NOT NULL
        GROUP BY p.character_id
        ORDER BY p.character_id ASC
    """, nativeQuery = true)
        // Tính điểm trung bình theo từng ký tự cho một người dùng
    List<CharAverageScoreRes>getAverageScoreByCharForUser(Long userId);

    // Admin Daily
    @Query(value = """
    SELECT
        DATE(p.created_at) AS date,
        ROUND(AVG(p.score), 1) AS averageScore,
        COUNT(p.id) AS totalPractice,
        COUNT(DISTINCT p.user_id) AS totalActiveUsers
    FROM practices p
    WHERE p.created_at >= (CURRENT_DATE - INTERVAL :days DAY)
    GROUP BY DATE(p.created_at)
    ORDER BY date ASC
""", nativeQuery = true)
    List<DailyScoreDateRes> getSystemDailyPracticeStats(int days);

    // Hard Char
    @Query(value = """
        SELECT 
            p.character_id AS characterId,
            p.predicted_label AS predictedLabel,
            ROUND(AVG(p.score), 1) AS averageScore,
            COUNT(p.id) AS totalAttempts
        FROM practices p
        WHERE p.character_id IS NOT NULL
        GROUP BY p.character_id
        ORDER BY averageScore ASC
        LIMIT :limit
    """, nativeQuery = true)
    List<LowestCharScoreRes> getLowestScoreCharacters(int limit);
}
