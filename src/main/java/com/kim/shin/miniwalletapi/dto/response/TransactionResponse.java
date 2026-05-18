package com.kim.shin.miniwalletapi.dto.response;

import com.kim.shin.miniwalletapi.entity.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionResponse {

    private Long id;
    private String type;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String status;
    private String description;
    private Long targetWalletId;
    private LocalDateTime createdAt;

    public static TransactionResponse from(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType().name())
                .amount(transaction.getAmount())
                .balanceBefore(transaction.getBalanceBefore())
                .balanceAfter(transaction.getBalanceAfter())
                .status(transaction.getStatus().name())
                .description(transaction.getDescription())
                .targetWalletId(transaction.getTargetWallet() != null
                        ? transaction.getTargetWallet().getId()
                        : null)
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}