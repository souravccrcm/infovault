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
    private final UserLoginLogRepository userLoginLogRepository;

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

    @Override
    public ArticleListResponse getAllArticles() {

        Long userId = 1L; // later from SecurityContext/SSO

        // Get last session
        Optional<UserLoginLog> sessionOpt =
                userLoginLogRepository.findTopByUserIdOrderByLoginTimeDesc(userId);

        LocalDateTime fromTime = null;
        LocalDateTime lastLogoutTime = null;

        if (sessionOpt.isPresent()) {
            UserLoginLog log = sessionOpt.get();

            lastLogoutTime = log.getLogoutTime();

            fromTime = (log.getLogoutTime() != null)
                    ? log.getLogoutTime()
                    : log.getLoginTime();
        }

        //  Fetch active articles
        List<ArticleResponse> articles = articleRepository.findByActiveTrue()
                .stream()
                .map(ArticleMapper::toResponse)
                .toList();

        long totalCount;
        List<ArticleTypeCountDTO> typeCounts = new ArrayList<>();

        // Count logic
        if (fromTime != null) {

            totalCount = articleRepository.countNewArticles(fromTime);

            List<Object[]> rows =
                    articleRepository.countByArticleType(fromTime);

            for (Object[] row : rows) {
                typeCounts.add(
                        new ArticleTypeCountDTO(
                                (String) row[0],
                                (Long) row[1]
                        )
                );
            }

        } else {
            // First-time login
            totalCount = articleRepository.countByActiveTrue();
        }

        //  Build final response
        return new ArticleListResponse(lastLogoutTime,
                totalCount,
                typeCounts,
                articles
        );
    }


}