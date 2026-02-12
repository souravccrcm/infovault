package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.request.MasterRequest;
import com.ccrcm.infovault.dto.response.MasterResponse;
import com.ccrcm.infovault.service.UpdateTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/infovault/update-types")
@RequiredArgsConstructor
public class UpdateTypeController {

    private final UpdateTypeService service;

    @PostMapping
    public MasterResponse create(@RequestBody MasterRequest request) {
        return service.save(request);
    }

    @PutMapping("/{id}")
    public MasterResponse update(@PathVariable Long id,
                                 @RequestBody MasterRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public MasterResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<MasterResponse> getAll() {
        return service.getAll();
    }
}
