package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.entity.Article;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.mapper.ArticleMapper;
import com.ccrcm.infovault.repository.ArticleRepository;
import com.ccrcm.infovault.service.RcmUpdateService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse<List<RcmUpdateResponse>>> getAllRcmUpdateListDetails() {

        List<RcmUpdateResponse> articles = articleRepository.findByActiveTrue()
                .stream()
                .map(ArticleMapper::toRcmUpdateResponse)
                .toList();

        ApiResponse<List<RcmUpdateResponse>> body = new ApiResponse<>(
                true,
                "Rcm Updated fetched successfully",
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
