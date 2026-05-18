package com.kim.shin.miniwalletapi.service;

import com.kim.shin.miniwalletapi.dto.request.DepositRequest;
import com.kim.shin.miniwalletapi.dto.request.TransferRequest;
import com.kim.shin.miniwalletapi.dto.request.WithdrawRequest;
import com.kim.shin.miniwalletapi.dto.response.TransactionResponse;
import com.kim.shin.miniwalletapi.dto.response.WalletResponse;

import java.util.List;

public interface WalletService {

    TransactionResponse deposit(Long userId, DepositRequest request);

    TransactionResponse withdraw(Long userId, WithdrawRequest request);

    TransactionResponse transfer(Long userId, TransferRequest request);

    WalletResponse getBalance(Long userId);                        // ← Thêm mới

    List<TransactionResponse> getTransactionHistory(Long userId);  // ← Thêm mới
}