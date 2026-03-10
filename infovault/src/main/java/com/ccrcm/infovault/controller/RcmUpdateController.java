package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.service.RcmUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rcmUpdates")
@RequiredArgsConstructor
public class RcmUpdateController {

    private final RcmUpdateService rcmUpdateService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<RcmUpdateResponse>>> getAllRcmUpdateList(
            @RequestParam(required = false) List<Long> countryIds,
            @RequestParam(required = false) List<Long> updateTypeIds,
            @RequestParam(required = false) List<Long> clinicalTypeIds,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return rcmUpdateService.getAllRcmUpdateListDetails(
                countryIds, updateTypeIds, clinicalTypeIds, search, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RcmUpdateResponse>> getArticleById(@PathVariable Long id) {
        return rcmUpdateService.getRcmUpdateById(id);
    }

}
