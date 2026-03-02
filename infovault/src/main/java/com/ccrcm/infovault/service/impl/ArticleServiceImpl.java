package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.request.ArticleRequest;
import com.ccrcm.infovault.dto.response.ArticleDocumentResponse;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.dto.response.MediaResponse;
import com.ccrcm.infovault.entity.*;
import com.ccrcm.infovault.enums.ArticleStatus;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.mapper.ArticleMapper;
import com.ccrcm.infovault.repository.*;
import com.ccrcm.infovault.service.ArticleService;
import com.ccrcm.infovault.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final ImpactLevelRepository impactLevelRepository;
    private final ArticleImageRepository articleImageRepository;
    private final ArticleVideoRepository articleVideoRepository;
    private final ArticleDocumentRepository articleDocumentRepository;

    @Value("${file.storage.path}")
    private String storagePath;

    @Override
    public ArticleResponse save(ArticleRequest request,  List<MultipartFile> documents, List<MultipartFile> images,
                                List<MultipartFile> videos) throws IOException {

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
        ImpactLevel impactLevel= impactLevelRepository.findById(request.getImpactTypeId())
                .orElseThrow(() -> new BadRequestException("Invalid impact level"));

        // Set article fields
        article.setTitle(request.getTitle());
        article.setSource(source);
        article.setCountry(country);
        article.setUpdateType(updateType);
        article.setClinicalType(clinicalType);
        article.setImpactLevel(impactLevel);
        article.setArticleContent(request.getArticleContent());

        article.setStatus(
                request.getStatus() != null
                        ? request.getStatus()
                        : ArticleStatus.DRAFT
        );

        article.setUploadedBy(request.getUploadedBy());
        article.setActive(true);

//        // File Handling
//        if (file != null && !file.isEmpty()) {
//            log.info("Uploading file for article: {}", request.getTitle());
//
//            String filePath = FileUtil.saveDocument(storagePath, file);
//
//            article.setFileName(file.getOriginalFilename());
//            article.setFilePath(filePath);
//            article.setFileSize(file.getSize());
//        }

        Article saved = articleRepository.save(article);

        log.info("Article saved successfully. ID: {}", saved.getId());

        Long articleId = saved.getId();


        // ================= DOCUMENT UPLOAD =================
        if (documents != null && !documents.isEmpty()) {

            for (MultipartFile doc : documents) {

                if (!doc.isEmpty()) {

                    String docPath =
                            FileUtil.saveDocument(storagePath, articleId, doc);

                    ArticleDocument document = new ArticleDocument();
                    document.setArticle(saved);
                    document.setFileName(saved.getId() + "_" + doc.getOriginalFilename());
                    document.setFilePath(docPath);
                    document.setFileSize(doc.getSize());
                    document.setActive(true);

                    saved.getDocuments().add(document);
                }
            }
        }

        // ================= IMAGE UPLOAD =================
        if (images != null && !images.isEmpty()) {

            for (MultipartFile img : images) {

                if (!img.isEmpty()) {

                    String imagePath = FileUtil.saveImage(storagePath, articleId, img);

                    ArticleImage image = new ArticleImage();
                    image.setArticle(saved);
                    image.setFileName(articleId + "_" + img.getOriginalFilename());
                    image.setFilePath(imagePath);
                    image.setFileSize(img.getSize());
                    image.setActive(true);

                    saved.getImages().add(image);
                }
            }
        }

// ================= VIDEO UPLOAD =================
        if (videos != null && !videos.isEmpty()) {

            for (MultipartFile video : videos) {

                if (!video.isEmpty()) {

                    String videoPath = FileUtil.saveVideo(storagePath, articleId, video);

                    ArticleVideo articleVideo = new ArticleVideo();
                    articleVideo.setArticle(saved);
                    articleVideo.setFileName(articleId + "_" + video.getOriginalFilename());
                    articleVideo.setFilePath(videoPath);
                    articleVideo.setFileSize(video.getSize());
                    articleVideo.setActive(true);

                    saved.getVideos().add(articleVideo);
                }
            }
        }
//===================================================
        // Save again (Cascade saves children)
        Article finalSaved = articleRepository.save(saved);

        return ArticleMapper.toResponse(finalSaved);
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
    public void deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BadRequestException("No IDs provided for deletion");
        }

        log.info("Soft deleting articles with IDs: {}", ids);

        List<Article> articles = articleRepository.findAllById(ids);
        if (articles.isEmpty()) {
            throw new BadRequestException("No matching articles found for provided IDs");
        }

        // Validate that all requested IDs were found (optional strict behavior)
        if (articles.size() != ids.size()) {
            List<Long> foundIds = articles.stream().map(Article::getId).collect(Collectors.toList());
            List<Long> missing = ids.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toList());
            throw new BadRequestException("Articles not found for IDs: " + missing);
        }

        articles.forEach(article -> article.setActive(false));
        articleRepository.saveAll(articles);

        log.info("Articles deleted successfully. IDs: {}", ids);
    }

    @Override
    public void updateStatus(Long id, ArticleStatus status) {
        log.info("Updating status for article ID: {} to {}", id, status);

        if (status == null) {
            throw new BadRequestException("Status is required");
        }

        Article article = articleRepository.findById(id)
                .filter(Article::getActive)
                .orElseThrow(() -> new BadRequestException("Article not found"));

        article.setStatus(status);
        articleRepository.save(article);

        log.info("Article status updated. ID: {} -> {}", id, status);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> downloadFile(Long documentId) throws IOException {

        ArticleDocument document = articleDocumentRepository.findById(documentId)
                .filter(ArticleDocument::getActive)
                .orElseThrow(() -> new BadRequestException("Document not found"));

        Path path = Paths.get(document.getFilePath());
        UrlResource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new BadRequestException("File not found on server");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + document.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Override
    public List<ArticleDocumentResponse> getDocumentsByArticle(Long articleId) {
        List<ArticleDocument> documents =
                articleDocumentRepository
                        .findByArticleIdAndActiveTrue(articleId);

        return documents.stream()
                .map(doc -> {

                    String originalFileName = doc.getFileName();

                    if (originalFileName != null && originalFileName.contains("_")) {
                        originalFileName = originalFileName.substring(
                                originalFileName.indexOf("_") + 1
                        );
                    }

                    return new ArticleDocumentResponse(
                            doc.getId(),
                            originalFileName,
                            doc.getFileSize(),
                            "/api/documents/download/" + doc.getId()
                    );
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleResponse> getTop10ByCountry(Long countryId) {

        log.info("Fetching top 10 articles for country ID: {}", countryId);

        Pageable pageable = PageRequest.of(0, 10);

        List<Article> articles =
                articleRepository.findByCountryIdAndActiveTrueOrderByCreatedAtDesc(
                        countryId,
                        pageable
                );

        return articles.stream()
                .map(ArticleMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteDocument(Long documentId) {
        ArticleDocument document = articleDocumentRepository
                .findByIdAndActiveTrue(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        document.setActive(false);

        articleDocumentRepository.save(document);
    }
}
