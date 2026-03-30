package com.ladi.stour.service;

import com.ladi.stour.dto.LoginRequest;
import com.ladi.stour.dto.LoginResponse;
import com.ladi.stour.dto.RegisterRequest;
import com.ladi.stour.entity.UserEntity;

public interface InterfaceUserService {
    UserEntity register(RegisterRequest req);
    LoginResponse login(LoginRequest req);
    UserEntity getUserById(String id);
    UserEntity getUserByUsername(String username);
}
