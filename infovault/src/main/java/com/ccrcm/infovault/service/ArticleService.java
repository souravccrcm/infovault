package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.request.ArticleRequest;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {

    ArticleResponse save(ArticleRequest request, MultipartFile file) throws IOException;

    ArticleResponse getById(Long id);

    List<ArticleResponse> getAll();

    void delete(Long id);
}
