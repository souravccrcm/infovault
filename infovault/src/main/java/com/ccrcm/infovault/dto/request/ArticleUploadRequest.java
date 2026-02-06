package com.ccrcm.infovault.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleUploadRequest  {

    private Long id;   // null for new, used for update

    @NotBlank
    private String title;

    @NotBlank
    private String source;

    @NotBlank
    private String country;

    @NotBlank
    private String type;

    private String clinicalType;
    private String status;
    private String actions;
}
