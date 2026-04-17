package com.ccrcm.infovault.dto.response;

import lombok.Data;

import java.util.Set;
@Data
public class LoginResponse {

    private Long userId;
    private String email;
    private String role;
    private Set<String> permissions;
    private String firstName;
    private String lastName;

    private String accessToken;
    private String refreshToken;

}

