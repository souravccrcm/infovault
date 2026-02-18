package com.ccrcm.infovault.mapper;

import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.entity.Article;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

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
    //  ADD THIS METHOD
    public static RcmUpdateResponse toRcmUpdateResponse(Article article) {

        String base64File = null;

        try {
            if (article.getFilePath() != null) {
                byte[] fileBytes = Files.readAllBytes(Path.of(article.getFilePath()));
                base64File = Base64.getEncoder().encodeToString(fileBytes);
            }
        } catch (Exception e) {
            e.printStackTrace(); // or log properly
        }

        return new RcmUpdateResponse(
                article.getId(),
                article.getTitle(),
                article.getSource() != null ? article.getSource().getName() : null,
                article.getCountry() != null ? article.getCountry().getName() : null,
                article.getUpdateType() != null ? article.getUpdateType().getName() : null,
                article.getClinicalType() != null ? article.getClinicalType().getName() : null,
                article.getArticleContent() != null ? article.getArticleContent() : null,
                base64File, // return base64
                article.getCreatedAt() != null ? article.getCreatedAt() : null,
                article.getImpactLevel() != null ? article.getImpactLevel().getName() : null,
                article.getFileName(),
                article.getFileSize()
                );
    }


}
