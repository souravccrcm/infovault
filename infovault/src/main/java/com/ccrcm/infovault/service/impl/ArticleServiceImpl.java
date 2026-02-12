package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.request.ArticleUploadRequest;
import com.ccrcm.infovault.dto.response.ArticleListResponse;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.dto.response.ArticleTypeCountDTO;
import com.ccrcm.infovault.entity.Article;
import com.ccrcm.infovault.entity.UserLoginLog;
import com.ccrcm.infovault.enums.ArticleStatus;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.mapper.ArticleMapper;
import com.ccrcm.infovault.repository.ArticleRepository;
import com.ccrcm.infovault.repository.UserLoginLogRepository;
import com.ccrcm.infovault.service.ArticleService;
import com.ccrcm.infovault.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        Article article;

        // ===== CREATE vs UPDATE =====
        if (request.getId() != null) {
            article = articleRepository.findById(request.getId())
                    .orElseThrow(() -> new BadRequestException("Article not found"));
        } else {
            article = new Article();
            article.setUploadedBy(1L); // later from SSO
            article.setActive(true);
        }

        // ===== FILE handling (only required for CREATE) =====
        if (file != null && !file.isEmpty()) {
            String filePath;
            try {
                filePath = FileUtil.saveFile(storagePath, file);
            } catch (Exception ex) {
                throw new BadRequestException("Failed to save file");
            }

            article.setFileName(file.getOriginalFilename());
            article.setFilePath(filePath);
            article.setFileSize(file.getSize());
        } else if (request.getId() == null) {
            throw new BadRequestException("File must not be empty");
        }

        // ===== COMMON FIELDS =====
        article.setTitle(request.getTitle());
        article.setSource(request.getSource());
        article.setCountry(request.getCountry());
        article.setArticleType(request.getType());
        article.setClinicalType(request.getClinicalType());

        // default status if not provided
        article.setStatus(
                request.getStatus() != null
                        ? request.getStatus()
                        : ArticleStatus.DRAFT
        );

        Article saved = articleRepository.save(article);
        return ArticleMapper.toResponse(saved);
    }




}