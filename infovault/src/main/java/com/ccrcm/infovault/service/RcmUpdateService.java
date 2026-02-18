package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.response.ArticleListResponse;
import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;

import java.util.List;
import org.springframework.http.ResponseEntity;

public interface RcmUpdateService {

    ResponseEntity<ApiResponse<List<RcmUpdateResponse>>> getAllRcmUpdateListDetails();

    ResponseEntity<ApiResponse<RcmUpdateResponse>> getRcmUpdateById(Long id);
}
