package com.thameem.ecommerce.service;

import com.thameem.ecommerce.dto.AuthResponse;
import com.thameem.ecommerce.dto.LoginRequest;
import com.thameem.ecommerce.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
