package com.ladi.stour.controller;

import com.ladi.stour.dto.LoginRequest;
import com.ladi.stour.dto.LoginResponse;
import com.ladi.stour.dto.RegisterRequest;
import com.ladi.stour.entity.UserEntity;
import com.ladi.stour.security.JwtTokenProvider;
import com.ladi.stour.service.InterfaceUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final InterfaceUserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        try {
            UserEntity user = userService.register(req);
            String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new LoginResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getFullName(),
                            user.getRole(),
                            token,
                            "User registered successfully"
                    ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(null, null, null, null, null, null, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest req) {
        try {
            LoginResponse response = userService.login(req);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, null, null, null, null, e.getMessage()));
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            UserEntity user = userService.getUserById(id);
            return ResponseEntity.ok(new LoginResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole(),
                    null,
                    "User retrieved successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginResponse(null, null, null, null, null, null, e.getMessage()));
        }
    }
}
