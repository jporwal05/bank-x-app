package com.bankx.controllers;

import com.bankx.entity.Account;
import com.bankx.entity.AccountType;
import com.bankx.entity.Transaction;
import com.bankx.models.dto.AccountDetailsResponse;
import com.bankx.models.dto.TransferRequest;
import com.bankx.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
    public ResponseEntity<AccountDetailsResponse> transferFunds(
            @PathVariable("fromAccountId") Long fromAccountId,
            @PathVariable("toAccountId") Long toAccountId,
            @RequestBody TransferRequest transferRequest
    ) {
        AccountDetailsResponse accountDetailsResponse = accountService.transferFunds(fromAccountId, toAccountId, transferRequest.getAmount());
        return ResponseEntity.ok(accountDetailsResponse);
    }

    @PostMapping("/{accountId}/{accountType}/deposit")
    public ResponseEntity<Transaction> deposit(
            @PathVariable("accountId") Long accountId,
            @PathVariable("accountType") AccountType accountType,
            @RequestParam("amount") Double amount
    ) {
        Transaction incomingTransaction = accountService.deposit(accountId, BigDecimal.valueOf(amount), accountType);
        return ResponseEntity.ok(incomingTransaction);
    }

    @PostMapping("/{accountId}/{accountType}/withdraw")
    public ResponseEntity<Transaction> withdraw(
            @PathVariable("accountId") Long accountId,
            @PathVariable("accountType") AccountType accountType,
            @RequestParam("amount") Double amount
    ) {
        Transaction outgoingTransaction = accountService.withdraw(accountId, BigDecimal.valueOf(amount), accountType);
        return ResponseEntity.ok(outgoingTransaction);
    }
}
