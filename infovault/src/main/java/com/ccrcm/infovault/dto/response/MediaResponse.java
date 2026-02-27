package com.ccrcm.infovault.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class MediaResponse {
    private Long id;
    private String fileName;
    private String url;
    private Long fileSize;
}
