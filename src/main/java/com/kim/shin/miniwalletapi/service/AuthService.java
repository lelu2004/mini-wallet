package com.kim.shin.miniwalletapi.service;

import com.kim.shin.miniwalletapi.dto.request.LoginRequest;
import com.kim.shin.miniwalletapi.dto.request.RefreshTokenRequest;
import com.kim.shin.miniwalletapi.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    void logout(Long userId);
}