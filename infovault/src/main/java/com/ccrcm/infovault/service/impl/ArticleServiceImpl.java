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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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

        Article article = request.getId() != null
                ? articleRepository.findById(request.getId())
                .orElseThrow(() -> new BadRequestException("Article not found"))
                : new Article();

        Source source = sourceRepository.findById(request.getSourceId())
                .orElseThrow(() -> new BadRequestException("Invalid source"));

        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new BadRequestException("Invalid country"));

        UpdateType updateType = updateTypeRepository.findById(request.getUpdateTypeId())
                .orElseThrow(() -> new BadRequestException("Invalid update type"));

        ClinicalType clinicalType = clinicalTypeRepository.findById(request.getClinicalTypeId())
                .orElseThrow(() -> new BadRequestException("Invalid clinical type"));

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

        if (file != null && !file.isEmpty()) {
            String filePath = FileUtil.saveFile(storagePath, file);
            article.setFileName(file.getOriginalFilename());
            article.setFilePath(filePath);
            article.setFileSize(file.getSize());
        }

        article.setActive(true);

        Article saved = articleRepository.save(article);
        return ArticleMapper.toResponse(saved);
    }

    @Override
    public ArticleResponse getById(Long id) {
        Article article = articleRepository.findById(id)
                .filter(Article::getActive)
                .orElseThrow(() -> new BadRequestException("Article not found"));

        return ArticleMapper.toResponse(article);
    }

    @Override
    public List<ArticleResponse> getAll() {
        return articleRepository.findAllByActiveTrue()
                .stream()
                .map(ArticleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Article not found"));

        article.setActive(false);
        articleRepository.save(article);
    }
}
