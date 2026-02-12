package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.request.MasterRequest;
import com.ccrcm.infovault.dto.response.MasterResponse;
import com.ccrcm.infovault.entity.ClinicalType;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.repository.ClinicalTypeRepository;
import com.ccrcm.infovault.service.ClinicalTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicalTypeServiceImpl implements ClinicalTypeService {

    private final ClinicalTypeRepository repository;

    @Override
    public MasterResponse save(MasterRequest request) {

        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Clinical Type already exists");
        }

        ClinicalType entity = new ClinicalType();
        entity.setName(request.getName());

        return map(repository.save(entity));
    }

    @Override
    public MasterResponse update(Long id, MasterRequest request) {

        ClinicalType entity = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Clinical Type not found"));

        entity.setName(request.getName());

        return map(repository.save(entity));
    }

    @Override
    public void delete(Long id) {

        ClinicalType entity = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Clinical Type not found"));

        entity.setActive(false);
        repository.save(entity);
    }

    @Override
    public MasterResponse getById(Long id) {

        ClinicalType entity = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Clinical Type not found"));

        return map(entity);
    }

    @Override
    public List<MasterResponse> getAll() {

        return repository.findAll()
                .stream()
                .filter(ClinicalType::isActive)
                .map(this::map)
                .collect(Collectors.toList());
    }


    private MasterResponse map(ClinicalType entity) {
        MasterResponse response = new MasterResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setActive(entity.isActive());
        return response;
    }
}
