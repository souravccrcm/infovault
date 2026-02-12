package com.ccrcm.infovault.mapper;

import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.entity.Article;

public class ArticleMapper {

    public static ArticleResponse toResponse(Article article) {

        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())

                .sourceId(article.getSource().getId())
                .sourceName(article.getSource().getName())

                .countryId(article.getCountry().getId())
                .countryName(article.getCountry().getName())

                .updateTypeId(article.getUpdateType().getId())
                .updateTypeName(article.getUpdateType().getName())

                .clinicalTypeId(article.getClinicalType().getId())
                .clinicalTypeName(article.getClinicalType().getName())

                .articleContent(article.getArticleContent())
                .status(article.getStatus())

                .fileName(article.getFileName())
                .fileSize(article.getFileSize())

                .uploadedBy(article.getUploadedBy())

                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }
    // ✅ ADD THIS METHOD
    public static RcmUpdateResponse toRcmUpdateResponse(Article article) {

        return new RcmUpdateResponse(
                article.getId(),
                article.getTitle(),
                article.getSource().getName(),
                article.getCountry().getName(),
                article.getUpdateType().getName(),
                article.getClinicalType().getName()
        );
    }


}
