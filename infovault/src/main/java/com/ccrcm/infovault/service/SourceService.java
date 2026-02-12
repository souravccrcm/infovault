package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.request.SourceRequest;
import com.ccrcm.infovault.dto.response.SourceResponse;

import java.util.List;

public interface SourceService {

    SourceResponse save(SourceRequest request);

    SourceResponse getById(Long id);

    List<SourceResponse> getAll();

    void delete(Long id);
}