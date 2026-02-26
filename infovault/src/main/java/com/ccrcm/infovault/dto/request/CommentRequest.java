package com.ccrcm.infovault.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    private Long articleId;
    private Long parentId;
    private String content;
}