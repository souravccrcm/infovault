package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.request.ArticleUploadRequest;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.entity.Article;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.repository.ArticleRepository;
import com.ccrcm.infovault.service.ArticleService;
import com.ccrcm.infovault.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Value("${file.storage.path}")
    private String storagePath;

    @Override
    public ArticleResponse uploadArticle(
            ArticleUploadRequest request,
            MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File must not be empty");
        }

        String filePath;
        try {
            filePath = FileUtil.saveFile(storagePath, file);
        } catch (Exception ex) {
            throw new BadRequestException("Failed to save file");
        }

        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setSource(request.getSource());
        article.setCountry(request.getCountry());
        article.setType(request.getType());
        article.setClinicalType(request.getClinicalType());
        article.setStatus(request.getStatus());
        article.setActions(request.getActions());
        article.setFilePath(filePath);

        Article saved = articleRepository.save(article);

        return mapToResponse(saved);
    }

    private ArticleResponse mapToResponse(Article article) {
        ArticleResponse response = new ArticleResponse();
        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setSource(article.getSource());
        response.setCountry(article.getCountry());
        response.setType(article.getType());
        response.setClinicalType(article.getClinicalType());
        response.setStatus(article.getStatus());
        response.setActions(article.getActions());
        response.setFilePath(article.getFilePath());
        return response;
    }
}
