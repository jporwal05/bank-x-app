package com.bankx.models.dto;

import com.bankx.entity.CurrentAccount;
import com.bankx.entity.SavingsAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDetailsResponse {
    private String token;
    private SavingsAccount savingsAccount;
    private CurrentAccount currentAccount;
}
