package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.StrokeOptionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrokeOptionDataRep extends JpaRepository<StrokeOptionData, Long> {
}
