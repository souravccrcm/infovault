package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.request.MasterRequest;
import com.ccrcm.infovault.dto.response.MasterResponse;
import com.ccrcm.infovault.entity.Country;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.repository.CountryRepository;
import com.ccrcm.infovault.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;

    @Override
    public MasterResponse save(MasterRequest request) {

        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Country already exists");
        }

        Country country = new Country();
        country.setName(request.getName());

        return map(repository.save(country));
    }

    @Override
    public MasterResponse update(Long id, MasterRequest request) {

        Country country = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Country not found"));

        country.setName(request.getName());

        return map(repository.save(country));
    }

    @Override
    public void delete(Long id) {

        Country country = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Country not found"));

        // Soft delete
        country.setActive(false);
        repository.save(country);
    }

    @Override
    public MasterResponse getById(Long id) {

        Country country = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Country not found"));

        return map(country);
    }

    @Override
    public List<MasterResponse> getAll() {

        return repository.findAll()
                .stream()
                .filter(Country::isActive)
                .map(this::map)
                .collect(Collectors.toList());
    }

    private MasterResponse map(Country country) {
        MasterResponse response = new MasterResponse();
        response.setId(country.getId());
        response.setName(country.getName());
        response.setActive(country.isActive());
        return response;
    }
}
