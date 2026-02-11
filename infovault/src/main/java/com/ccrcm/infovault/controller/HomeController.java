package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.response.ArticleListResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.service.ArticleService;
import com.ccrcm.infovault.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/infovault/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ApiResponse<ArticleListResponse> getAllArticles() {

        return new ApiResponse<>(
                true,
                "Articles fetched successfully",
                homeService.getAllArticles()
        );
    }
}
