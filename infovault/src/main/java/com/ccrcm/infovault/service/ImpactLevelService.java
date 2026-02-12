package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.request.MasterRequest;
import com.ccrcm.infovault.dto.response.MasterResponse;

import java.util.List;

public interface ImpactLevelService {

    MasterResponse save(MasterRequest request);

    MasterResponse update(Long id, MasterRequest request);

    void delete(Long id);

    MasterResponse getById(Long id);

    List<MasterResponse> getAll();
}
