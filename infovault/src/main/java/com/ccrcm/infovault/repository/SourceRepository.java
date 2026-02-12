package com.ccrcm.infovault.repository;


import com.ccrcm.infovault.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SourceRepository extends JpaRepository<Source, Long> {

    List<Source> findAllByActiveTrue();

    Optional<Source> findByNameIgnoreCase(String name);
}


