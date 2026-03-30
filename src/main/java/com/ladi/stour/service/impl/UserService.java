package com.ladi.stour.service.impl;

import com.ladi.stour.dto.LoginRequest;
import com.ladi.stour.dto.LoginResponse;
import com.ladi.stour.dto.RegisterRequest;
import com.ladi.stour.entity.UserEntity;
import com.ladi.stour.repository.UserRepository;
import com.ladi.stour.security.JwtTokenProvider;
import com.ladi.stour.service.InterfaceUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements InterfaceUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserEntity register(RegisterRequest req) {
        // Check if username already exists
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        UserEntity user = UserEntity.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .fullName(req.getFullName())
                .isActive(true)
                .role("USER")
                .build();

        return userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        UserEntity user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Username or password is incorrect"));

        // Verify password
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Username or password is incorrect");
        }

        // Check if user is active
        if (!user.getIsActive()) {
            throw new RuntimeException("User account is inactive");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                token,
                "Login successful"
        );
    }

    @Override
    public UserEntity getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
