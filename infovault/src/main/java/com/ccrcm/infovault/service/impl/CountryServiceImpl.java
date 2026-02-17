package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.request.MasterRequest;
import com.ccrcm.infovault.dto.response.MasterResponse;
import com.ccrcm.infovault.entity.Country;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.repository.CountryRepository;
import com.ccrcm.infovault.service.CountryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Transactional
public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;

    @Override
    public MasterResponse save(MasterRequest request) {

        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Country already exists");
        }

        Country country = new Country();
        country.setName(request.getName());
        country.setActive(true);

        return map(repository.save(country));
    }

    @Override
    public MasterResponse update(Long id, MasterRequest request) {

        Country country = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new BadRequestException("Country not found"));

        if (repository.existsByNameIgnoreCase(request.getName())
                && !country.getName().equalsIgnoreCase(request.getName())) {
            throw new BadRequestException("Country already exists");
        }

        country.setName(request.getName());

        return map(repository.save(country));
    }

    @Override
    public void delete(Long id) {

        Country country = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new BadRequestException("Country not found"));

        country.setActive(false);
        repository.save(country);
    }

    @Override
    public MasterResponse getById(Long id) {

        Country country = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new BadRequestException("Country not found"));

        return map(country);
    }

    @Override
    public List<MasterResponse> getAll() {

        return repository.findByActiveTrue()
                .stream()
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
