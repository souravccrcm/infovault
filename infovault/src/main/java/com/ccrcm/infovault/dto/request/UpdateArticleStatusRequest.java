
package com.ccrcm.infovault.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateArticleStatusRequest {
    @NotBlank(message = "Status is required")
    private String status;
}
