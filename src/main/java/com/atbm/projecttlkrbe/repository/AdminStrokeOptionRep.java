package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.AdminStrokeOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminStrokeOptionRep extends JpaRepository<AdminStrokeOption, Long> {
    List<AdminStrokeOption> findTop3ByAdminStrokeDataIdOrderByIdDesc(Long strokeDataId);
}
