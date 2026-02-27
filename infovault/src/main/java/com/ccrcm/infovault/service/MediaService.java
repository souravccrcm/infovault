package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.response.ArticleMediaResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface MediaService {
    ArticleMediaResponse getMediaByArticleId(Long articleId);
//    ResponseEntity<Resource> getImageById(Long id) throws IOException;
//
//    ResponseEntity<Resource> getVideoById(Long id) throws IOException;
}