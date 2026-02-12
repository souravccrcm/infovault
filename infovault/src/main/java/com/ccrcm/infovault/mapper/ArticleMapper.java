package com.ccrcm.infovault.mapper;

import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.dto.response.RcmUpdateResponse;
import com.ccrcm.infovault.entity.Article;
import com.ccrcm.infovault.util.CountryUtil;

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
        response.setCountryCode(CountryUtil.toIso2(article.getCountry())); // Country mapping (name + ISO2)
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

    public static RcmUpdateResponse toRcmUpdateResponse(Article article) {
        if (article == null) {
            return null;
        }

        return new RcmUpdateResponse(
                article.getId(),
                article.getTitle(),
                article.getSource(),
                article.getCountry(),
                article.getArticleType(),
                article.getClinicalType()
        );
    }

}
