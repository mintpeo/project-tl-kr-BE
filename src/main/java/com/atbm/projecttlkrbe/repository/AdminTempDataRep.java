package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.AdminTempData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminTempDataRep extends JpaRepository<AdminTempData, Long> {
    List<AdminTempData> findByCharacterId(Long charId);
}
