package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {


    // Fetch only active records
    List<Country> findByActiveTrue();

    // Fetch active record by ID
    Optional<Country> findByIdAndActiveTrue(Long id);

    // Check duplicate name (case insensitive)
    boolean existsByNameIgnoreCase(String name);
}