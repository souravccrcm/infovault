package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.request.MasterRequest;
import com.ccrcm.infovault.dto.response.MasterResponse;
import com.ccrcm.infovault.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
@Slf4j
public class CountryController {

    private final CountryService service;

    @PostMapping
    public MasterResponse create(@RequestBody MasterRequest request) {
        log.info("**********CountryController----->CREATE_COUNTRY");

        try {
            MasterResponse response = service.save(request);
            log.info("Country created successfully with name: {}", request.getName());
            return response;
        } catch (Exception e) {
            log.error("Error while creating country", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public MasterResponse update(@PathVariable Long id,
                                 @RequestBody MasterRequest request) {
        log.info("**********CountryController----->UPDATE_COUNTRY");

        try {
            MasterResponse response = service.update(id, request);
            log.info("Country updated successfully with id: {}", id);
            return response;
        } catch (Exception e) {
            log.error("Error while updating country with id: {}", id, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("**********CountryController----->DELETE_COUNTRY");

        try {
            service.delete(id);
            log.info("Country deleted successfully with id: {}", id);
        } catch (Exception e) {
            log.error("Error while deleting country with id: {}", id, e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public MasterResponse getById(@PathVariable Long id) {
        log.info("**********CountryController----->GET_COUNTRY_BY_ID");

        try {
            MasterResponse response = service.getById(id);
            log.info("Country fetched successfully with id: {}", id);
            return response;
        } catch (Exception e) {
            log.error("Error while fetching country with id: {}", id, e);
            throw e;
        }
    }

    @GetMapping
    public List<MasterResponse> getAll() {
        log.info("**********CountryController----->GET_ALL_COUNTRIES");

        try {
            List<MasterResponse> response = service.getAll();
            log.info("Fetched all countries successfully. Count: {}", response.size());
            return response;
        } catch (Exception e) {
            log.error("Error while fetching all countries", e);
            throw e;
        }
    }
}
