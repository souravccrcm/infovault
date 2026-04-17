package com.ccrcm.infovault.service.impl;
import com.ccrcm.infovault.dto.request.CreateUserRequest;
import com.ccrcm.infovault.dto.request.LoginRequest;
import com.ccrcm.infovault.dto.request.RefreshTokenRequest;
import com.ccrcm.infovault.dto.response.LoginResponse;
import com.ccrcm.infovault.dto.response.UserResponse;
import com.ccrcm.infovault.entity.Permission;
import com.ccrcm.infovault.entity.Role;
import com.ccrcm.infovault.entity.User;
import com.ccrcm.infovault.exception.BadRequestException;
import com.ccrcm.infovault.exception.ResourceNotFoundException;
import com.ccrcm.infovault.repository.RoleRepository;
import com.ccrcm.infovault.repository.UserRepository;
import com.ccrcm.infovault.security.JwtUtil;
import com.ccrcm.infovault.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setActive(true);
        user.setRole(role);

        userRepository.save(user);

        return mapToUserResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        if (request.getEmail() == null || request.getPassword() == null) {
            throw new BadCredentialsException("Invalid credentials");
        }

        //  1. LDAP Authentication
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        if (!authentication.isAuthenticated()) {
//            throw new BadCredentialsException("Invalid credentials");
//        }

        //  2. Get user from DB (MANDATORY in your case)
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found in system"));

        //  3. Extract role
        String role = user.getRole() != null
                ? user.getRole().getName()
                : "USER";

        //  4. Extract permissions
        Set<String> permissions = new HashSet<>();

        if (user.getRole() != null && user.getRole().getPermissions() != null) {
            permissions = user.getRole().getPermissions()
                    .stream()
                    .map(Permission::getCode)
                    .collect(Collectors.toSet());
        }

        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(),
                user.getId(),
                role
        );

        String refreshToken = jwtUtil.generateRefreshToken(
                user.getEmail(),
                user.getId()
        );

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(role);
        response.setPermissions(permissions);
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        // 🔥 THIS LINE VALIDATES TOKEN (throws exception if invalid)
        Claims claims = jwtUtil.getClaims(refreshToken);

        if (!jwtUtil.isRefreshToken(refreshToken)) {
            throw new BadRequestException("Invalid token type");
        }

        String email = jwtUtil.getUsername(refreshToken);
        Long userId = jwtUtil.getUserId(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String role = user.getRole() != null ? user.getRole().getName() : "USER";

        String newAccessToken = jwtUtil.generateAccessToken(email, userId, role);

        LoginResponse response = new LoginResponse();
        response.setUserId(userId);
        response.setEmail(email);
        response.setRole(role);
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }


//    @Override
//    public LoginResponse login(LoginRequest request) {
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!Boolean.TRUE.equals(user.getActive())) {
//            throw new RuntimeException("User is inactive");
//        }
//
//        Set<String> permissionCodes = user.getRole()
//                .getPermissions()
//                .stream()
//                .map(Permission::getCode)
//                .collect(Collectors.toSet());
//
//        LoginResponse response = new LoginResponse();
//        response.setUserId(user.getId());
//        response.setEmail(user.getEmail());
//        response.setRole(user.getRole().getName());
//        response.setFirstName(user.getFirstName());
//        response.setLastName(user.getLastName());
//        response.setPermissions(permissionCodes);
//
//        return response;
//    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole().getName());
        return response;
    }
}
