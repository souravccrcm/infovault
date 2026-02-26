package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.request.ArticleRequest;
import com.ccrcm.infovault.dto.response.ArticleMediaResponse;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.enums.ArticleStatus;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {

    ArticleResponse save(
            ArticleRequest request,
            MultipartFile file,
            List<MultipartFile> images,
            List<MultipartFile> videos
    ) throws IOException;

    ArticleResponse getById(Long id);

    List<ArticleResponse> getTop10ByCountry(Long countryId);

    List<ArticleResponse> getAll();

    void deleteByIds(List<Long> id);

    void updateStatus(Long id, ArticleStatus status);

    ResponseEntity<Resource> downloadFile(Long id) throws IOException;

    ArticleMediaResponse getMediaByArticleId(Long articleId);
}
