package com.ccrcm.infovault.service.impl;
import com.ccrcm.infovault.dto.request.CreateUserRequest;
import com.ccrcm.infovault.dto.request.LoginRequest;
import com.ccrcm.infovault.dto.response.LoginResponse;
import com.ccrcm.infovault.dto.response.UserResponse;
import com.ccrcm.infovault.entity.Permission;
import com.ccrcm.infovault.entity.Role;
import com.ccrcm.infovault.entity.User;
import com.ccrcm.infovault.repository.RoleRepository;
import com.ccrcm.infovault.repository.UserRepository;
import com.ccrcm.infovault.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new RuntimeException("User is inactive");
        }

        Set<String> permissionCodes = user.getRole()
                .getPermissions()
                .stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().getName());
        response.setPermissions(permissionCodes);

        return response;
    }

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
