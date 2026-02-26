package com.ccrcm.infovault.dto.response;

import lombok.Data;

@Data
public class MediaResponse {
    private Long id;
    private String fileName;
    private String filePath;
    private Long fileSize;
}
