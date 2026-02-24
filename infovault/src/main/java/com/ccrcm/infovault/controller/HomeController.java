package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.response.ArticleListResponse;
import com.ccrcm.infovault.dto.response.CountryArticleCountDTO;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<ApiResponse<ArticleListResponse>> getAllArticles() {

        ApiResponse<ArticleListResponse> body = new ApiResponse<>(
                true,
                "Articles fetched successfully",
                homeService.getAllArticles()
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/articles-count-by-region")
    public ResponseEntity<ApiResponse<List<CountryArticleCountDTO>>> getNewArticlesByRegion() {
        // Includes ISO2 short code from CountryUtil along with country name and count
        ApiResponse<List<CountryArticleCountDTO>> body = new ApiResponse<>(
                true,
                "Region-wise new article counts fetched successfully",
                homeService.getNewArticleCountsByRegion()
        );
        return ResponseEntity.ok(body);
    }
}
