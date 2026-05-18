package com.kim.shin.miniwalletapi.service.impl;

import com.kim.shin.miniwalletapi.dto.request.DepositRequest;
import com.kim.shin.miniwalletapi.dto.request.TransferRequest;
import com.kim.shin.miniwalletapi.dto.request.WithdrawRequest;
import com.kim.shin.miniwalletapi.dto.response.TransactionResponse;
import com.kim.shin.miniwalletapi.dto.response.WalletResponse;
import com.kim.shin.miniwalletapi.entity.Transaction;
import com.kim.shin.miniwalletapi.entity.Wallet;
import com.kim.shin.miniwalletapi.enums.TransactionStatus;
import com.kim.shin.miniwalletapi.enums.TransactionType;
import com.kim.shin.miniwalletapi.repository.TransactionRepository;
import com.kim.shin.miniwalletapi.repository.WalletRepository;
import com.kim.shin.miniwalletapi.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransactionResponse deposit(Long userId, DepositRequest request) {
        Wallet wallet = getWalletByUserId(userId);

        BigDecimal balanceBefore = wallet.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(request.getAmount());

        wallet.setBalance(balanceAfter);
        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .build();

        return TransactionResponse.from(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(Long userId, WithdrawRequest request) {
        Wallet wallet = getWalletByUserId(userId);

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        BigDecimal balanceBefore = wallet.getBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(request.getAmount());

        wallet.setBalance(balanceAfter);
        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .type(TransactionType.WITHDRAW)
                .amount(request.getAmount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .build();

        return TransactionResponse.from(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public TransactionResponse transfer(Long userId, TransferRequest request) {
        Wallet senderWallet = getWalletByUserId(userId);
        Wallet receiverWallet = walletRepository.findById(request.getTargetWalletId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Target wallet not found: " + request.getTargetWalletId()));

        if (senderWallet.getId().equals(receiverWallet.getId())) {
            throw new IllegalArgumentException("Cannot transfer to the same wallet");
        }

        if (senderWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Trừ ví người gửi
        BigDecimal senderBalanceBefore = senderWallet.getBalance();
        BigDecimal senderBalanceAfter = senderBalanceBefore.subtract(request.getAmount());
        senderWallet.setBalance(senderBalanceAfter);
        walletRepository.save(senderWallet);

        // Cộng ví người nhận
        BigDecimal receiverBalanceBefore = receiverWallet.getBalance();
        BigDecimal receiverBalanceAfter = receiverBalanceBefore.add(request.getAmount());
        receiverWallet.setBalance(receiverBalanceAfter);
        walletRepository.save(receiverWallet);

        // Lưu transaction từ phía người gửi
        Transaction transaction = Transaction.builder()
                .wallet(senderWallet)
                .targetWallet(receiverWallet)
                .type(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .balanceBefore(senderBalanceBefore)
                .balanceAfter(senderBalanceAfter)
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .build();

        return TransactionResponse.from(transactionRepository.save(transaction));
    }


    private Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Wallet not found for user: " + userId));
    }
    @Override
    public WalletResponse getBalance(Long userId) {
        Wallet wallet = getWalletByUserId(userId);
        return WalletResponse.from(wallet);
    }

    @Override
    public List<TransactionResponse> getTransactionHistory(Long userId) {
        Wallet wallet = getWalletByUserId(userId);
        return transactionRepository
                .findByWalletIdOrderByCreatedAtDesc(wallet.getId())
                .stream()
                .map(TransactionResponse::from)
                .toList();
    }

}