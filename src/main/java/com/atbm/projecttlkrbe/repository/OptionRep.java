package com.atbm.projecttlkrbe.repository;

import com.atbm.projecttlkrbe.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRep extends JpaRepository<Option, Long> {
    Optional<Option> findByIdAndQuestion_Id(long optionId, long questionId);
    List<Option> findByQuestion_Id(long questionId);
}
