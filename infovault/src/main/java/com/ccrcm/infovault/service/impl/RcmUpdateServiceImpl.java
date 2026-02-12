package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.entity.Article;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.mapper.ArticleMapper;
import com.ccrcm.infovault.repository.ArticleRepository;
import com.ccrcm.infovault.service.RcmUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // ✅ FIX for Lazy loading issue
public class RcmUpdateServiceImpl implements RcmUpdateService {

    private final ArticleRepository articleRepository;

    @Override
    public ApiResponse<List<RcmUpdateResponse>> getAllRcmUpdateListDetails() {

        List<RcmUpdateResponse> articles = articleRepository.findByActiveTrue()
                .stream()
                .map(ArticleMapper::toRcmUpdateResponse)
                .toList();

        return new ApiResponse<>(
                true,
                "Rcm Updated fetched successfully",
                articles
        );
    }

    @Override
    public ApiResponse<RcmUpdateResponse> getRcmUpdateById(Long id) {

        Article article = articleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Article not found with id: " + id)
                );

        RcmUpdateResponse response = ArticleMapper.toRcmUpdateResponse(article);

        return new ApiResponse<>(
                true,
                "Article fetched successfully",
                response
        );
    }
}
