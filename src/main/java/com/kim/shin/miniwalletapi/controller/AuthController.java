package com.kim.shin.miniwalletapi.controller;

import com.kim.shin.miniwalletapi.dto.request.LoginRequest;
import com.kim.shin.miniwalletapi.dto.request.RefreshTokenRequest;
import com.kim.shin.miniwalletapi.dto.response.AuthResponse;
import com.kim.shin.miniwalletapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<Map<String, String>> logout(@PathVariable Long userId) {
        authService.logout(userId);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}