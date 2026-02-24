package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.response.ArticleListResponse;
import com.ccrcm.infovault.dto.response.CountryArticleCountDTO;

import java.util.List;

public interface HomeService {
    ArticleListResponse getAllArticles();

    List<CountryArticleCountDTO> getNewArticleCountsByRegion();
}
