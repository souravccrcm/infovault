package com.ccrcm.infovault.dto.request;

import com.ccrcm.infovault.enums.ArticleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequest {

    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private Long sourceId;

    @NotNull
    private Long countryId;

    @NotNull
    private Long updateTypeId;

    @NotNull
    private Long clinicalTypeId;

    private String articleContent;

    private ArticleStatus status;

    @NotNull
    private Long uploadedBy; // From frontend (temporary approach)
}
