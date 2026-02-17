package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.request.MasterRequest;
import com.ccrcm.infovault.dto.response.MasterResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.service.ClinicalTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinical-types")
@RequiredArgsConstructor
@Slf4j
public class ClinicalTypeController {

    private final ClinicalTypeService service;

    //  CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<MasterResponse>> create(
            @Valid @RequestBody MasterRequest request) {

        log.info("Creating ClinicalType with name: {}", request.getName());

        MasterResponse response = service.save(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Clinical Type created successfully",
                        response
                )
        );
    }

    //  UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MasterResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody MasterRequest request) {

        log.info("Updating ClinicalType with ID: {}", id);

        MasterResponse response = service.update(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Clinical Type updated successfully",
                        response
                )
        );
    }

    //  DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        log.info("Deleting ClinicalType with ID: {}", id);

        service.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Clinical Type deleted successfully",
                        null
                )
        );
    }

    //  GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MasterResponse>> getById(@PathVariable Long id) {

        log.info("Fetching ClinicalType with ID: {}", id);

        MasterResponse response = service.getById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Clinical Type fetched successfully",
                        response
                )
        );
    }

    //  GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<MasterResponse>>> getAll() {

        log.info("Fetching all ClinicalTypes");

        List<MasterResponse> list = service.getAll();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Clinical Types fetched successfully",
                        list
                )
        );
    }
}
