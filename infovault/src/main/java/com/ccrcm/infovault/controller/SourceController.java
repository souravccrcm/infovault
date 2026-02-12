package com.ccrcm.infovault.controller;


import com.ccrcm.infovault.dto.request.SourceRequest;
import com.ccrcm.infovault.dto.response.SourceResponse;
import com.ccrcm.infovault.service.SourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sources")
@RequiredArgsConstructor
public class SourceController {

    private final SourceService sourceService;

    @PostMapping
    public SourceResponse save(@Valid @RequestBody SourceRequest request) {
        return sourceService.save(request);
    }

    @GetMapping("/{id}")
    public SourceResponse getById(@PathVariable Long id) {
        return sourceService.getById(id);
    }

    @GetMapping
    public List<SourceResponse> getAll() {
        return sourceService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sourceService.delete(id);
    }
}

