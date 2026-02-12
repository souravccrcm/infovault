package com.ccrcm.infovault.service.impl;


import com.ccrcm.infovault.dto.request.SourceRequest;
import com.ccrcm.infovault.dto.response.SourceResponse;
import com.ccrcm.infovault.entity.Source;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.repository.SourceRepository;
import com.ccrcm.infovault.service.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;

    @Override
    public SourceResponse save(SourceRequest request) {

        Source source;

        // UPDATE
        if (request.getId() != null) {
            source = sourceRepository.findById(request.getId())
                    .orElseThrow(() -> new BadRequestException("Source not found"));
        } else {
            // CREATE
            if (sourceRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
                throw new BadRequestException("Source already exists");
            }
            source = new Source();
        }

        source.setName(request.getName());
        source.setActive(true);

        Source saved = sourceRepository.save(source);

        return SourceResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    public SourceResponse getById(Long id) {
        Source source = sourceRepository.findById(id)
                .filter(Source::getActive)
                .orElseThrow(() -> new BadRequestException("Source not found"));

        return SourceResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }

    @Override
    public List<SourceResponse> getAll() {
        return sourceRepository.findAllByActiveTrue()
                .stream()
                .map(source -> SourceResponse.builder()
                        .id(source.getId())
                        .name(source.getName())
                        .createdAt(source.getCreatedAt())
                        .updatedAt(source.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Source source = sourceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Source not found"));

        source.setActive(false); // Soft delete
        sourceRepository.save(source);
    }
}

