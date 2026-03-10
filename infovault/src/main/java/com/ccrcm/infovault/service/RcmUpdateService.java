package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.response.ArticleListResponse;
import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface RcmUpdateService {

    ResponseEntity<ApiResponse<Page<RcmUpdateResponse>>> getAllRcmUpdateListDetails(
            List<Long> countryIds,
            List<Long> updateTypeIds,
            List<Long> clinicalTypeIds,
            String search,
            int page,
            int size
    );

    ResponseEntity<ApiResponse<RcmUpdateResponse>> getRcmUpdateById(Long id);
}
