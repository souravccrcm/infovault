package com.ccrcm.infovault.dto.request;

import com.ccrcm.infovault.enums.ArticleStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleUploadRequest {

    private Long id; // null = create, not null = update

    @NotBlank
    private String title;

    @NotBlank
    private String source;

    @NotBlank
    private String country;

    @NotBlank
    private String type;

    private String clinicalType;

    private ArticleStatus status; // ✅ enum now

    private String actions;
}
