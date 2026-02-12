package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.entity.ImpactLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpactLevelRepository extends JpaRepository<ImpactLevel, Long> {

    boolean existsByNameIgnoreCase(String name);
}
