package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.request.ArticleRequest;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    // CREATE / UPDATE
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ArticleResponse>> save(
            @ParameterObject @ModelAttribute ArticleRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {

        log.info("Creating/Updating article. Title: {}", request.getTitle());

        ArticleResponse response = articleService.save(request, file);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Article saved successfully",
                        response
                )
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ArticleResponse>> getById(@PathVariable Long id) {

        log.info("Fetching article with ID: {}", id);

        ArticleResponse response = articleService.getById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Article fetched successfully",
                        response
                )
        );
    }

    //  GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> getAll() {

        log.info("Fetching all articles");

        List<ArticleResponse> articles = articleService.getAll();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Articles fetched successfully",
                        articles
                )
        );
    }

    // DELETE (Soft Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        log.info("Deleting article with ID: {}", id);

        articleService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Article deleted successfully",
                        null
                )
        );
    }

    // DOWNLOAD FILE
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {

        log.info("Downloading file for article ID: {}", id);

        return articleService.downloadFile(id);
    }
}
