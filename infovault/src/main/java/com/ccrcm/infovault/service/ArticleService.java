package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.request.ArticleRequest;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {

    ArticleResponse save(ArticleRequest request, MultipartFile file) throws IOException;

    ArticleResponse getById(Long id);

    List<ArticleResponse> getAll();

    void deleteByIds(List<Long> id);

    ResponseEntity<Resource> downloadFile(Long id) throws IOException;
}
