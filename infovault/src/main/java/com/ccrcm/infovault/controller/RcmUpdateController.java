package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.service.RcmUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/infovault/rcmUpdates")
@RequiredArgsConstructor
public class RcmUpdateController {

    private final RcmUpdateService rcmUpdateService;

    @GetMapping
    public ApiResponse<List<RcmUpdateResponse>> getAllRcmUpdateList() {

        return rcmUpdateService.getAllRcmUpdateListDetails();
    }

    @GetMapping("/{id}")
    public ApiResponse<RcmUpdateResponse> getArticleById(@PathVariable Long id) {
        return rcmUpdateService.getRcmUpdateById(id);
    }

}
