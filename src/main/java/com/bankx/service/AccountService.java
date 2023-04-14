package com.bankx.service;

import com.bankx.entity.Account;
import com.bankx.entity.AccountType;
import com.bankx.entity.CurrentAccount;
import com.bankx.entity.SavingsAccount;
import com.bankx.entity.Transaction;
import com.bankx.entity.TransactionCategory;
import com.bankx.entity.TransactionType;
import com.bankx.entity.User;
import com.bankx.models.dto.AccountDetailsResponse;
import com.bankx.models.dto.TransactionRequest;
import com.bankx.repository.AccountRepository;
import com.bankx.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final NotificationService notificationService;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
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

    public AccountDetailsResponse transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        if (fromAccount instanceof CurrentAccount) {
            BigDecimal withdrawnAmount = fromAccount.withdraw(amount);
            BigDecimal depositedAmount = toAccount.deposit(amount);
            Transaction outgoingTransaction = Transaction.builder()
                    .account(fromAccount)
                    .type(TransactionType.WITHDRAWAL)
                    .amount(amount)
                    .calculatedAmount(withdrawnAmount)
                    .dateTime(LocalDateTime.now())
                    .reference("Transfer to " + toAccount.getId())
                    .transactionCategory(TransactionCategory.RETAIL)
                    .transactionFee(fromAccount.getTransactionFee())
                    .build();
            Transaction incomingTransaction = Transaction.builder()
                    .account(toAccount)
                    .type(TransactionType.DEPOSIT)
                    .amount(amount)
                    .calculatedAmount(depositedAmount)
                    .dateTime(LocalDateTime.now())
                    .reference("Transfer from " + fromAccount.getId())
                    .transactionCategory(TransactionCategory.RETAIL)
                    .transactionFee(fromAccount.getTransactionFee())
                    .build();

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            transactionRepository.save(outgoingTransaction);
            transactionRepository.save(incomingTransaction);
            notificationService.sendTransactionNotification(fromAccount.getUser(), outgoingTransaction);
            notificationService.sendTransactionNotification(toAccount.getUser(), incomingTransaction);
            return AccountDetailsResponse.builder()
                    .currentAccount((CurrentAccount) (fromAccount.getAccountType().equals(AccountType.CURRENT) ? fromAccount : toAccount))
                    .savingsAccount((SavingsAccount) (fromAccount.getAccountType().equals(AccountType.SAVINGS) ? fromAccount : toAccount))
                    .build();
        } else {
            throw new RuntimeException("Savings accounts are not eligible to transfer funds");
        }
    }

    // overload the deposit method to take a datetime parameter
    public Transaction deposit(Long accountId, BigDecimal amount, AccountType accountType, TransactionRequest transactionRequest) {
        Account account = accountRepository.findByIdAndAccountType(accountId, accountType);
        BigDecimal depositedAmount = account.deposit(amount);
        accountRepository.save(account);
        Transaction incomingTransaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .calculatedAmount(depositedAmount)
                .dateTime(transactionRequest.getDateTime())
                .reference(transactionRequest.getReference())
                .transactionCategory(transactionRequest.getTransactionCategory())
                .transactionFee(account.getTransactionFee())
                .build();
        transactionRepository.save(incomingTransaction);
        notificationService.sendTransactionNotification(account.getUser(), incomingTransaction);
        return incomingTransaction;
    }

    public Transaction deposit(Long accountId, BigDecimal amount, AccountType accountType) {
        return deposit(accountId, amount, accountType, TransactionRequest.builder()
                .transactionCategory(TransactionCategory.RETAIL)
                .dateTime(LocalDateTime.now()).build());
    }

    // overload the withdraw method to take a datetime parameter
    public Transaction withdraw(Long accountId, BigDecimal amount, AccountType accountType, TransactionRequest transactionRequest) {
        Account account = accountRepository.findByIdAndAccountType(accountId, accountType);
        BigDecimal withdrawnAmount = account.withdraw(amount);
        accountRepository.save(account);
        Transaction outgoingTransaction = Transaction.builder()
                .account(account)
                .type(TransactionType.WITHDRAWAL)
                .amount(amount)
                .calculatedAmount(withdrawnAmount)
                .dateTime(transactionRequest.getDateTime())
                .reference(transactionRequest.getReference())
                .transactionCategory(transactionRequest.getTransactionCategory())
                .transactionFee(account.getTransactionFee())
                .build();
        transactionRepository.save(outgoingTransaction);
        notificationService.sendTransactionNotification(account.getUser(), outgoingTransaction);
        return outgoingTransaction;
    }

    public Transaction withdraw(Long accountId, BigDecimal amount, AccountType accountType) {
        return withdraw(accountId, amount, accountType, TransactionRequest.builder()
                .transactionCategory(TransactionCategory.RETAIL)
                .dateTime(LocalDateTime.now()).build());
    }

    public List<Transaction> processTransactions(List<TransactionRequest> transactions) {
        List<Transaction> savedTransactions = new ArrayList<>();
        for (TransactionRequest transaction : transactions) {
            Long accountId = transaction.getAccountId();
            AccountType accountType = transaction.getAccountType();
            if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
                savedTransactions.add(deposit(accountId, transaction.getAmount(), accountType,
                        transaction));
            } else if (transaction.getTransactionType() == TransactionType.WITHDRAWAL) {
                savedTransactions.add(withdraw(accountId, transaction.getAmount(), accountType,
                        transaction));
            }
        }
        return savedTransactions;
    }

    public List<Transaction> getAllTransactionsByAccountId(Long accountId) {
        return transactionRepository.findAllByAccountId(accountId);
    }

    public List<Transaction> getAllTransactionsByCategory(TransactionCategory category) {
        return transactionRepository.findAllByTransactionCategory(category);
    }
}
