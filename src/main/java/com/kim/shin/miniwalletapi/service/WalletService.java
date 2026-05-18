package com.kim.shin.miniwalletapi.service;

import com.kim.shin.miniwalletapi.dto.request.DepositRequest;
import com.kim.shin.miniwalletapi.dto.request.TransferRequest;
import com.kim.shin.miniwalletapi.dto.request.WithdrawRequest;
import com.kim.shin.miniwalletapi.dto.response.TransactionResponse;

public interface WalletService {

    TransactionResponse deposit(Long userId, DepositRequest request);

    TransactionResponse withdraw(Long userId, WithdrawRequest request);

    TransactionResponse transfer(Long userId, TransferRequest request);
}