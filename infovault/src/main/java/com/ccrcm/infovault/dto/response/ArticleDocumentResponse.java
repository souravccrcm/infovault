package com.ccrcm.infovault.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleDocumentResponse {
    private Long id;
    private String fileName;
    private Long fileSize;
    private String downloadUrl;
}
