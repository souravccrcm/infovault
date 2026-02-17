package com.ccrcm.infovault.dto.response;

import com.ccrcm.infovault.enums.ArticleStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ArticleResponse {

    private Long id;
    private String title;

    private Long sourceId;
    private String sourceName;

    private Long countryId;
    private String countryName;

    private Long updateTypeId;
    private String updateTypeName;

    private Long clinicalTypeId;
    private String clinicalTypeName;

    private Long impactTypeId;
    private String impactTypeName;

    private String articleContent;

    private ArticleStatus status;

    private String fileName;
    private Long fileSize;

    private Long uploadedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
