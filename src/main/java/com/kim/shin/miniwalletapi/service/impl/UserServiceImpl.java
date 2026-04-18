package com.kim.shin.miniwalletapi.service.impl;

import com.kim.shin.miniwalletapi.dto.request.RegisterRequest;
import com.kim.shin.miniwalletapi.dto.response.UserResponse;
import com.kim.shin.miniwalletapi.entity.User;
import com.kim.shin.miniwalletapi.entity.Wallet;
import com.kim.shin.miniwalletapi.enums.UserStatus;
import com.kim.shin.miniwalletapi.enums.WalletStatus;
import com.kim.shin.miniwalletapi.repository.UserRepository;
import com.kim.shin.miniwalletapi.repository.WalletRepository;
import com.kim.shin.miniwalletapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        // TODO Phase 2: thay bằng BCryptPasswordEncoder khi thêm Spring Security
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(request.getPassword())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        Wallet wallet = Wallet.builder()
                .user(savedUser)
                .balance(BigDecimal.ZERO)
                .status(WalletStatus.ACTIVE)
                .build();

        Wallet savedWallet = walletRepository.save(wallet);

        return UserResponse.from(savedUser, savedWallet);
    }
}