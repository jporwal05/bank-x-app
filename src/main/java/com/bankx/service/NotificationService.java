package com.bankx.service;

import com.bankx.entity.Transaction;
import com.bankx.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendTransactionNotification(User user, Transaction transaction) {
        log.info("Transaction notification sent to user {} for transaction {}",
                user.getId(), transaction.getId());
    }
}
