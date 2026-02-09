package com.ccrcm.infovault.mapper;

import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.entity.Article;

public class ArticleMapper {

    private ArticleMapper() {
        // utility class
    }

    public static ArticleResponse toResponse(Article article) {
        if (article == null) {
            return null;
        }

        ArticleResponse response = new ArticleResponse();
        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setSource(article.getSource());
        response.setCountry(article.getCountry());
        response.setType(article.getArticleType());
        response.setClinicalType(article.getClinicalType());
        response.setStatus(article.getStatus());
        response.setFileName(article.getFileName());
        response.setFilePath(article.getFilePath());
        response.setFileSize(article.getFileSize());
        response.setUploadedBy(article.getUploadedBy());
        response.setActive(article.getActive());
        response.setCreatedAt(article.getCreatedAt());
        response.setUpdatedAt(article.getUpdatedAt());

        return response;
    }
}
