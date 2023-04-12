package com.bankx.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_account_id")
    private CurrentAccount currentAccount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "savings_account_id")
    private SavingsAccount savingsAccount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_credentials_id")
    private UserCredentials userCredentials;
}
