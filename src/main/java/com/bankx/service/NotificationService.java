package com.bankx.service;

import com.bankx.entity.Transaction;
import com.bankx.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendTransactionNotification(User user, Transaction transaction) {
        log.info("user: {}, transaction type: {}, amount: {}, account type: {}, transaction category: {}",
                user.getEmail(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getAccount().getAccountType(),
                transaction.getTransactionCategory());
    }
}
