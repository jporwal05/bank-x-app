package com.bankx.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    }
}
