package com.kim.shin.miniwalletapi.dto.response;

import com.kim.shin.miniwalletapi.entity.User;
import com.kim.shin.miniwalletapi.entity.Wallet;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String status;
    private LocalDateTime createdAt;

    // Thông tin wallet
    private Long walletId;
    private BigDecimal balance;

    public static UserResponse from(User user, Wallet wallet) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt())
                .walletId(wallet.getId())
                .balance(wallet.getBalance())
                .build();
    }
}