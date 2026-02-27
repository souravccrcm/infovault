package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.request.ArticleRequest;
import com.ccrcm.infovault.dto.response.ArticleMediaResponse;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.dto.request.UpdateArticleStatusRequest;
import com.ccrcm.infovault.service.ArticleService;
import com.ccrcm.infovault.service.MediaService;
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

    private final MediaService mediaService;


    // CREATE / UPDATE
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ArticleResponse>> save(
            @ParameterObject @ModelAttribute ArticleRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videos
    ) throws IOException {

        log.info("Creating/Updating article. Title: {}", request.getTitle());

        ArticleResponse response = articleService.save(request, file, images, videos);

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

    // BULK DELETE - delete multiple articles by IDs
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteByIds(@RequestParam("ids") List<Long> ids) {

        log.info("Deleting articles with IDs: {}", ids);

        articleService.deleteByIds(ids);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Articles deleted successfully",
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

    // CHANGE STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> changeStatus(@PathVariable Long id, @RequestBody UpdateArticleStatusRequest request) {
        log.info("Updating status for article ID: {} to {}", id, request.getStatus());
        articleService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(new ApiResponse<>(true, "Article status updated successfully", null));
    }

//    @GetMapping("/media/{articleId}")
//    public ResponseEntity<ArticleMediaResponse> getArticleMedia(
//            @PathVariable Long articleId) {
//
//        ArticleMediaResponse response =
//                articleService.getMediaByArticleId(articleId);
//
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/{articleId}/media")
    public ResponseEntity<ArticleMediaResponse> getArticleMedia(
            @PathVariable Long articleId) {

        return ResponseEntity.ok(
                mediaService.getMediaByArticleId(articleId)
        );
    }

    @GetMapping("/country/{countryId}")
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> getTop10ByCountry(
            @PathVariable Long countryId) {

        List<ArticleResponse> articles =
                articleService.getTop10ByCountry(countryId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Top 10 articles fetched successfully",
                        articles
                )
        );
    }

}
