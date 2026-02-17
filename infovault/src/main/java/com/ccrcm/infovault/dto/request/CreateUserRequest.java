package com.ccrcm.infovault.dto.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private Long roleId;
}
