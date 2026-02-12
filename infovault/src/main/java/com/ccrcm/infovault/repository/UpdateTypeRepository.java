package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.entity.UpdateType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateTypeRepository extends JpaRepository<UpdateType, Long> {

    boolean existsByNameIgnoreCase(String name);
}
