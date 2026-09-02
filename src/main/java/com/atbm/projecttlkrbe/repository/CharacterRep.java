package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRep extends JpaRepository<CharacterEntity, Long> {
    @Query("SELECT c FROM CharacterEntity c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.transcription) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CharacterEntity> searchCharacters(@Param("keyword") String keyword);
}
