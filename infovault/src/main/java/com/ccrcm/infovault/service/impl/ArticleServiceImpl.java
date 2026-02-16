package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.request.ArticleRequest;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.entity.*;
import com.ccrcm.infovault.enums.ArticleStatus;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.mapper.ArticleMapper;
import com.ccrcm.infovault.repository.*;
import com.ccrcm.infovault.service.ArticleService;
import com.ccrcm.infovault.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final SourceRepository sourceRepository;
    private final CountryRepository countryRepository;
    private final UpdateTypeRepository updateTypeRepository;
    private final ClinicalTypeRepository clinicalTypeRepository;

    @Value("${file.storage.path}")
    private String storagePath;

    @Override
    public ArticleResponse save(ArticleRequest request, MultipartFile file) throws IOException {

        log.info("Saving article. Title: {}", request.getTitle());

        Article article;

        if (request.getId() == null || request.getId() == 0) {
            article = new Article();
        } else {
            article = articleRepository.findById(request.getId())
                    .orElseThrow(() -> new BadRequestException("Article not found"));
        }
        // Fetch related entities
        Source source = sourceRepository.findById(request.getSourceId())
                .orElseThrow(() -> new BadRequestException("Invalid source"));

        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new BadRequestException("Invalid country"));

        UpdateType updateType = updateTypeRepository.findById(request.getUpdateTypeId())
                .orElseThrow(() -> new BadRequestException("Invalid update type"));

        ClinicalType clinicalType = clinicalTypeRepository.findById(request.getClinicalTypeId())
                .orElseThrow(() -> new BadRequestException("Invalid clinical type"));

        // Set article fields
        article.setTitle(request.getTitle());
        article.setSource(source);
        article.setCountry(country);
        article.setUpdateType(updateType);
        article.setClinicalType(clinicalType);
        article.setArticleContent(request.getArticleContent());

        article.setStatus(
                request.getStatus() != null
                        ? request.getStatus()
                        : ArticleStatus.DRAFT
        );

        article.setUploadedBy(request.getUploadedBy());
        article.setActive(true);

        // File Handling
        if (file != null && !file.isEmpty()) {
            log.info("Uploading file for article: {}", request.getTitle());

            String filePath = FileUtil.saveFile(storagePath, file);

            article.setFileName(file.getOriginalFilename());
            article.setFilePath(filePath);
            article.setFileSize(file.getSize());
        }

        Article saved = articleRepository.save(article);

        log.info("Article saved successfully. ID: {}", saved.getId());

        return ArticleMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleResponse getById(Long id) {

        log.info("Fetching article with ID: {}", id);

        Article article = articleRepository.findById(id)
                .filter(Article::getActive)
                .orElseThrow(() -> new BadRequestException("Article not found"));

        return ArticleMapper.toResponse(article);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleResponse> getAll() {

        log.info("Fetching all active articles");

        return articleRepository.findAllByActiveTrue()
                .stream()
                .map(ArticleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {

        log.info("Soft deleting article with ID: {}", id);

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Article not found"));

        article.setActive(false);
        articleRepository.save(article);

        log.info("Article soft deleted successfully. ID: {}", id);
    }
}
