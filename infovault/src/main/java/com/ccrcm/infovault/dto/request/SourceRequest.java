package com.ccrcm.infovault.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceRequest {

    private Long id; // null = create

    @NotBlank(message = "Source name is required")
    private String name;
}
