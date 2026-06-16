package com.kim.shin.miniwalletapi.service.impl;

import com.kim.shin.miniwalletapi.dto.request.LoginRequest;
import com.kim.shin.miniwalletapi.dto.request.RefreshTokenRequest;
import com.kim.shin.miniwalletapi.dto.response.AuthResponse;
import com.kim.shin.miniwalletapi.entity.RefreshToken;
import com.kim.shin.miniwalletapi.entity.User;
import com.kim.shin.miniwalletapi.repository.RefreshTokenRepository;
import com.kim.shin.miniwalletapi.repository.UserRepository;
import com.kim.shin.miniwalletapi.security.JwtUtils;
import com.kim.shin.miniwalletapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        // Tìm user theo email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Kiểm tra password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Xóa refresh token cũ nếu có
        refreshTokenRepository.deleteAllByUserId(user.getId());

        // Tạo token mới
        String accessToken = jwtUtils.generateAccessToken(user.getEmail());
        String refreshTokenValue = UUID.randomUUID().toString();

        // Lưu refresh token vào DB
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(refreshTokenValue)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .build();

        refreshTokenRepository.save(refreshToken);

        return AuthResponse.of(accessToken, refreshTokenValue, user.getId(), user.getEmail());
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        // Tìm refresh token trong DB
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        // Kiểm tra còn hạn không
        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("Refresh token has expired, please login again");
        }

        // Tạo access token mới
        String newAccessToken = jwtUtils.generateAccessToken(refreshToken.getUser().getEmail());

        return AuthResponse.of(
                newAccessToken,
                refreshToken.getToken(),
                refreshToken.getUser().getId(),
                refreshToken.getUser().getEmail()
        );
    }

    @Override
    @Transactional
    public void logout(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        refreshTokenRepository.deleteAllByUserId(userId);
    }
}