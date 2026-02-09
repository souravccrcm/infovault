package com.ccrcm.infovault.dto.response;

import com.ccrcm.infovault.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ArticleResponse {

    private Long id;

    private String title;
    private String source;
    private String country;

    // article metadata
    private String type;
    private String clinicalType;
    private ArticleStatus status;

    // file info
    private String fileName;
    private String filePath;
    private Long fileSize;

    // audit info
    private Long uploadedBy;
    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}