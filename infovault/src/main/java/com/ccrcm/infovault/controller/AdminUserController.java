package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.request.CreateUserRequest;
import com.ccrcm.infovault.dto.response.UserResponse;
import com.ccrcm.infovault.globalResposeDto.ApiResponse;
import com.ccrcm.infovault.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        log.info("Creating new user. Email: {}", request.getEmail());

        UserResponse response = userService.createUser(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User created successfully",
                        response
                )
        );
    }

}
