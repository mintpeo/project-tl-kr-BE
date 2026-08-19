package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRep extends JpaRepository<CharacterEntity, Long> {
}
