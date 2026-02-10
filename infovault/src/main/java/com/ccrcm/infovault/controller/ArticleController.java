package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.request.ArticleUploadRequest;
import com.ccrcm.infovault.dto.response.ArticleListResponse;
import com.ccrcm.infovault.dto.response.ArticleResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ArticleResponse> uploadArticle(
            @RequestPart("metadata") @Valid ArticleUploadRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        return new ApiResponse<>(
                true,
                "Article uploaded successfully",
                articleService.uploadArticle(request, file)
        );
    }

}
