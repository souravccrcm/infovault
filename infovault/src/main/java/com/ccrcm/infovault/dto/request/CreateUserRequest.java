package com.ccrcm.infovault.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotNull(message = "Email is required")
    private String email;
    private String firstName;
    private String lastName;
    @NotNull(message = "Role is required")
    private Long roleId;
}
