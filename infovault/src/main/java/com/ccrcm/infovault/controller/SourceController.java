package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.request.SourceRequest;
import com.ccrcm.infovault.dto.response.SourceResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.service.SourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sources")
@RequiredArgsConstructor
@Slf4j
public class SourceController {

    private final SourceService sourceService;

    //  CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<SourceResponse>> save(
            @Valid @RequestBody SourceRequest request) {

        log.info("Creating source with name: {}", request.getName());

        SourceResponse response = sourceService.save(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Source created successfully",
                        response
                )
        );
    }

    //  GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SourceResponse>> getById(@PathVariable Long id) {

        log.info("Fetching source with ID: {}", id);

        SourceResponse response = sourceService.getById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Source fetched successfully",
                        response
                )
        );
    }

    //  GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<SourceResponse>>> getAll() {

        log.info("Fetching all sources");

        List<SourceResponse> list = sourceService.getAll();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Sources fetched successfully",
                        list
                )
        );
    }

    //  DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        log.info("Deleting source with ID: {}", id);

        sourceService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Source deleted successfully",
                        null
                )
        );
    }
}
