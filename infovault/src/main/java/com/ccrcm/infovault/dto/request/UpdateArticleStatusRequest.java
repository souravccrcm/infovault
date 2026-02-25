
package com.ccrcm.infovault.dto.request;

import com.ccrcm.infovault.enums.ArticleStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateArticleStatusRequest {
    @NotNull(message = "Status is required")
    private ArticleStatus status;
}
