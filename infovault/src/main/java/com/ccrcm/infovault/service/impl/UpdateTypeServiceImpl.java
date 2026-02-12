package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.request.MasterRequest;
import com.ccrcm.infovault.dto.response.MasterResponse;
import com.ccrcm.infovault.entity.UpdateType;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.repository.UpdateTypeRepository;
import com.ccrcm.infovault.service.UpdateTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateTypeServiceImpl implements UpdateTypeService {

    private final UpdateTypeRepository repository;

    @Override
    public MasterResponse save(MasterRequest request) {

        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Update Type already exists");
        }

        UpdateType entity = new UpdateType();
        entity.setName(request.getName());

        return map(repository.save(entity));
    }

    @Override
    public MasterResponse update(Long id, MasterRequest request) {

        UpdateType entity = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Update Type not found"));

        entity.setName(request.getName());

        return map(repository.save(entity));
    }

    @Override
    public void delete(Long id) {

        UpdateType entity = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Update Type not found"));

        entity.setActive(false);
        repository.save(entity);
    }

    @Override
    public MasterResponse getById(Long id) {

        UpdateType entity = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Update Type not found"));

        return map(entity);
    }

    @Override
    public List<MasterResponse> getAll() {

        return repository.findAll()
                .stream()
                .filter(UpdateType::isActive)
                .map(this::map)
                .collect(Collectors.toList());
    }

    private MasterResponse map(UpdateType entity) {
        MasterResponse response = new MasterResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setActive(entity.isActive());
        return response;
    }
}
