package com.bankx.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private BigDecimal transactionFee;

    protected Account(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        balance = balance.subtract(amount);
        applyTransactionFee(amount);
        return amount.subtract(transactionFee);
    }

    public BigDecimal deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        if (this instanceof SavingsAccount) {
            double interest = amount.doubleValue() * 0.005;
            this.balance = this.balance.add(BigDecimal.valueOf(interest));
        }
        applyTransactionFee(amount);
        return amount.subtract(transactionFee);
    }

    public BigDecimal applyTransactionFee(BigDecimal amount) {
        double fee = amount.doubleValue() * 0.0005;
        balance = balance.subtract(BigDecimal.valueOf(fee));
        transactionFee = BigDecimal.valueOf(fee);
        return transactionFee;
    }
}
