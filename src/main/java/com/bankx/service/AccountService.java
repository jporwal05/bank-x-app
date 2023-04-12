package com.bankx.service;

import com.bankx.entity.Account;
import com.bankx.entity.CurrentAccount;
import com.bankx.entity.SavingsAccount;
import com.bankx.entity.User;
import com.bankx.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public CurrentAccount createCurrentAccount(User user) {
        CurrentAccount currentAccount = new CurrentAccount(BigDecimal.ZERO);
        currentAccount.setUser(user);
        return accountRepository.save(currentAccount);
    }

    public SavingsAccount createSavingsAccount(User user) {
        SavingsAccount savingsAccount = new SavingsAccount(BigDecimal.valueOf(500));
        savingsAccount.setUser(user);
        return accountRepository.save(savingsAccount);
    }

    public void transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        if (fromAccount instanceof CurrentAccount) {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);

            if (toAccount instanceof SavingsAccount) {
                BigDecimal interest = amount.multiply(new BigDecimal("0.005"));
                toAccount.deposit(interest);
            }
        } else {
            throw new RuntimeException("Savings accounts are not eligible to transfer funds");
        }

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}
