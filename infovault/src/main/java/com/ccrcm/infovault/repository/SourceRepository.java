package com.ccrcm.infovault.repository;


import com.ccrcm.infovault.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {

    List<Source> findAllByActiveTrue();

    Optional<Source> findByNameIgnoreCase(String name);
}


