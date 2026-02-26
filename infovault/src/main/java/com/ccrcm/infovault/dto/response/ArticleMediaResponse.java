package com.ccrcm.infovault.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ArticleMediaResponse {
    private Long articleId;
    private List<MediaResponse> images;
    private List<MediaResponse> videos;
}
