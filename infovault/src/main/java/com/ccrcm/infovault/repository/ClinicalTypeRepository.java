package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.entity.ClinicalType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicalTypeRepository extends JpaRepository<ClinicalType, Long> {

    boolean existsByNameIgnoreCase(String name);
}
