package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.response.ArticleMediaResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface MediaService {
    ArticleMediaResponse getMediaByArticleId(Long articleId);

}