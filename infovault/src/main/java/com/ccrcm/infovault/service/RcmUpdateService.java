package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.response.ArticleListResponse;
import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;

import java.util.List;

public interface RcmUpdateService {

    ApiResponse<List<RcmUpdateResponse>> getAllRcmUpdateListDetails();

    ApiResponse<RcmUpdateResponse> getRcmUpdateById(Long id);
}
