package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.request.ArticleRequest;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
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

    // ✅ CREATE
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArticleResponse> save(
            @ParameterObject @ModelAttribute ArticleRequest request,
            @RequestPart("file") MultipartFile file
    ) {

        try {
            log.info("Creating article with title: {}", request.getTitle());

            ArticleResponse response = articleService.save(request, file);

            log.info("Article created successfully with ID: {}", response.getId());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("File upload failed for article: {}", request.getTitle(), e);
            return ResponseEntity.internalServerError().build();

        } catch (Exception e) {
            log.error("Error occurred while creating article", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getById(@PathVariable Long id) {

        try {
            log.info("Fetching article with ID: {}", id);

            ArticleResponse response = articleService.getById(id);

            log.info("Article fetched successfully for ID: {}", id);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("Article not found with ID: {}", id, e);
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            log.error("Error occurred while fetching article with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getAll() {

        try {
            log.info("Fetching all articles");

            List<ArticleResponse> articles = articleService.getAll();

            log.info("Fetched {} articles successfully", articles.size());

            return ResponseEntity.ok(articles);

        } catch (Exception e) {
            log.error("Error occurred while fetching all articles", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        try {
            log.info("Deleting article with ID: {}", id);

            articleService.delete(id);

            log.info("Article deleted successfully with ID: {}", id);

            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            log.error("Article not found for deletion with ID: {}", id, e);
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            log.error("Error occurred while deleting article with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
