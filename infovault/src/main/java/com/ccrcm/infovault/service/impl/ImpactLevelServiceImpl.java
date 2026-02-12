package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.request.MasterRequest;
import com.ccrcm.infovault.dto.response.MasterResponse;
import com.ccrcm.infovault.entity.ImpactLevel;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.repository.ImpactLevelRepository;
import com.ccrcm.infovault.service.ImpactLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImpactLevelServiceImpl implements ImpactLevelService {

    private final ImpactLevelRepository repository;

    @Override
    public MasterResponse save(MasterRequest request) {

        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Impact Level already exists");
        }

        ImpactLevel entity = new ImpactLevel();
        entity.setName(request.getName());

        return map(repository.save(entity));
    }

    @Override
    public MasterResponse update(Long id, MasterRequest request) {

        ImpactLevel entity = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Impact Level not found"));

        entity.setName(request.getName());

        return map(repository.save(entity));
    }

    @Override
    public void delete(Long id) {

        ImpactLevel entity = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Impact Level not found"));

        entity.setActive(false);
        repository.save(entity);
    }

    @Override
    public MasterResponse getById(Long id) {

        ImpactLevel entity = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Impact Level not found"));

        return map(entity);
    }

    @Override
    public List<MasterResponse> getAll() {

        return repository.findAll()
                .stream()
                .filter(ImpactLevel::isActive)
                .map(this::map)
                .collect(Collectors.toList());
    }

    private MasterResponse map(ImpactLevel entity) {
        MasterResponse response = new MasterResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setActive(entity.isActive());
        return response;
    }
}
