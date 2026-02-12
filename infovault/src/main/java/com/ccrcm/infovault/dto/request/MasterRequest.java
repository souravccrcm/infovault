package com.ccrcm.infovault.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MasterRequest {

    private Long id; // null = create

    @NotBlank
    private String name;
}