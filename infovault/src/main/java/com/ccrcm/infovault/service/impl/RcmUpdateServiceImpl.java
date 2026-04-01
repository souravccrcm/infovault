package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.entity.Article;
import com.ccrcm.infovault.enums.ArticleStatus;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.mapper.ArticleMapper;
import com.ccrcm.infovault.repository.ArticleRepository;
import com.ccrcm.infovault.service.RcmUpdateService;

import com.ccrcm.infovault.specification.ArticleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // ✅ FIX for Lazy loading issue
public class RcmUpdateServiceImpl implements RcmUpdateService {

    private final ArticleRepository articleRepository;

    @Override
    public ResponseEntity<ApiResponse<Page<RcmUpdateResponse>>> getAllRcmUpdateListDetails(
            List<Long> countryIds,
            List<Long> updateTypeIds,
            List<Long> clinicalTypeIds,
            String search,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        boolean hasCountryIds = countryIds != null && !countryIds.isEmpty();
        boolean hasUpdateTypeIds = updateTypeIds != null && !updateTypeIds.isEmpty();
        boolean hasClinicalTypeIds = clinicalTypeIds != null && !clinicalTypeIds.isEmpty();

        // IMPORTANT: pass dummy list if null (to avoid Hibernate error)
        if (!hasCountryIds) countryIds = List.of(-1L);
        if (!hasUpdateTypeIds) updateTypeIds = List.of(-1L);
        if (!hasClinicalTypeIds) clinicalTypeIds = List.of(-1L);

        // normalize search
        if (search != null && search.trim().isEmpty()) {
            search = null;
        }
        if (search != null) {
            search = search.toLowerCase();
        }

        Page<RcmUpdateResponse> articles = articleRepository.searchArticles(
                countryIds,
                updateTypeIds,
                clinicalTypeIds,
                hasCountryIds,
                hasUpdateTypeIds,
                hasClinicalTypeIds,
                search,
                ArticleStatus.PUBLISHED.getCode(),
                pageable
        ).map(ArticleMapper::toRcmUpdateResponse);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched successfully", articles)
        );
    }


    @Override
    public ResponseEntity<ApiResponse<RcmUpdateResponse>> getRcmUpdateById(Long id) {

        Article article = articleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Article not found with id: " + id)
                );

        RcmUpdateResponse response = ArticleMapper.toRcmUpdateResponse(article);

        ApiResponse<RcmUpdateResponse> body = new ApiResponse<>(
                true,
                "Article fetched successfully",
                response
        );
        return ResponseEntity.ok(body);
    }
}
