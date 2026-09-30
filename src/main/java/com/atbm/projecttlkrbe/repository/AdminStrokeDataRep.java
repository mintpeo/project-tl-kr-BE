package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.AdminStrokeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminStrokeDataRep extends JpaRepository<AdminStrokeData, Long> {
    Optional<AdminStrokeData> findByCharacterIdOrderByIdDesc(long id);
}
