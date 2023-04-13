package com.bankx.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("S")
public class SavingsAccount extends Account {

    private BigDecimal interestRate;

    public SavingsAccount(BigDecimal balance) {
        super(balance);
        this.interestRate = BigDecimal.ZERO;
        this.setAccountType(AccountType.SAVINGS);
    }
}
