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
import org.springframework.http.HttpStatus;
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

        Pageable pageable;

        if (search != null && !search.trim().isEmpty()) {
            // Do not apply sorting because Specification handles ranking
            pageable = PageRequest.of(page, size);
        } else {
            // Default sorting when no search
            pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        }

        Specification<Article> spec = ArticleSpecification.filterArticles(
                countryIds,
                updateTypeIds,
                clinicalTypeIds,
                search
        );

        Page<RcmUpdateResponse> articles = articleRepository
                .findAll(spec, pageable)
                .map(ArticleMapper::toRcmUpdateResponse);

        ApiResponse<Page<RcmUpdateResponse>> body = new ApiResponse<>(
                true,
                "Published Rcm Updates fetched successfully",
                articles
        );

        return ResponseEntity.ok(body);
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
