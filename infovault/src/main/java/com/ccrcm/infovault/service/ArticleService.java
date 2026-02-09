package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.request.ArticleUploadRequest;
import com.ccrcm.infovault.dto.response.ArticleListResponse;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {

    ArticleResponse uploadArticle(
            ArticleUploadRequest request,
            MultipartFile file
    );


    ArticleListResponse getAllArticles();
}