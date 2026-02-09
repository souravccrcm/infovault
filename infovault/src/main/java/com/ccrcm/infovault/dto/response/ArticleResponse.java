package com.ccrcm.infovault.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleResponse {

    private Long id;

    private String title;
    private String source;
    private String country;

    private String articleType;
    private String clinicalType;
    private String status;

    private String fileName;
}
