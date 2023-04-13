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
@DiscriminatorValue("C")
public class CurrentAccount extends Account {
    public CurrentAccount(BigDecimal balance) {
        super(balance);
        this.setAccountType(AccountType.CURRENT);
    }
}
