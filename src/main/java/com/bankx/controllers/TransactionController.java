package com.bankx.controllers;

import com.bankx.entity.Transaction;
import com.bankx.models.dto.TransactionRequest;
import com.bankx.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final AccountService accountService;

    @Autowired
    public TransactionController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/process")
    public ResponseEntity<List<Transaction>> processTransactions(@RequestBody List<TransactionRequest> transactions) throws AccountNotFoundException {
        List<Transaction> savedTransactions = accountService.processTransactions(transactions);
        return ResponseEntity.ok(savedTransactions);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<Transaction> transactions = accountService.getAllTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
}
