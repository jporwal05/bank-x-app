package com.bankx.controllers;

import com.bankx.models.dto.TransferRequest;
import com.bankx.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> transferFunds(
            @PathVariable("fromAccountId") Long fromAccountId,
            @PathVariable("toAccountId") Long toAccountId,
            @RequestBody TransferRequest transferRequest
    ) {
        log.info("Transfer request received from {} to {} for amount {}", fromAccountId, toAccountId, transferRequest.getAmount());
        accountService.transferFunds(fromAccountId, toAccountId, transferRequest.getAmount());
        return ResponseEntity.ok("Transfer completed successfully");
    }
}
