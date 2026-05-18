package com.kim.shin.miniwalletapi.controller;

import com.kim.shin.miniwalletapi.dto.request.DepositRequest;
import com.kim.shin.miniwalletapi.dto.request.TransferRequest;
import com.kim.shin.miniwalletapi.dto.request.WithdrawRequest;
import com.kim.shin.miniwalletapi.dto.response.TransactionResponse;
import com.kim.shin.miniwalletapi.dto.response.WalletResponse;
import com.kim.shin.miniwalletapi.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @PathVariable Long userId,
            @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(walletService.deposit(userId, request));
    }

    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @PathVariable Long userId,
            @Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(walletService.withdraw(userId, request));
    }

    @PostMapping("/{userId}/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @PathVariable Long userId,
            @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(walletService.transfer(userId, request));
    }
    @GetMapping("/{userId}/balance")
    public ResponseEntity<WalletResponse> getBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getBalance(userId));
    }

    @GetMapping("/{userId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(
            @PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getTransactionHistory(userId));
    }
}