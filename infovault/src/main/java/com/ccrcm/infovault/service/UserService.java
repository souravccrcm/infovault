package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.request.CreateUserRequest;
import com.ccrcm.infovault.dto.request.LoginRequest;
import com.ccrcm.infovault.dto.response.LoginResponse;
import com.ccrcm.infovault.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    LoginResponse login(LoginRequest request);
}
