package com.bankx.models.dto;

import com.bankx.entity.AccountType;
import com.bankx.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private Long accountId;
    private AccountType accountType;
    private TransactionType transactionType;
    private BigDecimal amount;
    private LocalDateTime dateTime;
}
