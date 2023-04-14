package com.bankx.controllers;

import com.bankx.entity.CurrentAccount;
import com.bankx.entity.SavingsAccount;
import com.bankx.entity.User;
import com.bankx.entity.UserCredentials;
import com.bankx.models.dto.AccountDetailsResponse;
import com.bankx.models.dto.AuthenticationResponse;
import com.bankx.models.dto.UserRegistrationRequest;
import com.bankx.security.JwtTokenUtil;
import com.bankx.security.JwtUserDetailsService;
import com.bankx.service.AccountService;
import com.bankx.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final AccountService accountService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUserDetailsService userDetailsService,
                          JwtTokenUtil jwtTokenUtil,
                          PasswordEncoder passwordEncoder,
                          UserService userService, AccountService accountService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) throws Exception {
        User user = User.builder()
                .email(userRegistrationRequest.getEmail())
                .firstName(userRegistrationRequest.getFirstName())
                .lastName(userRegistrationRequest.getLastName())
                .userCredentials(UserCredentials.builder()
                        .username(userRegistrationRequest.getUsername())
                        .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                        .build())
                .accounts(new ArrayList<>())
                .build();

        userService.save(user);
        CurrentAccount currentAccount = accountService.createCurrentAccount(user);
        SavingsAccount savingsAccount = accountService.createSavingsAccount(user);
        user.addAccount(currentAccount);
        user.addAccount(savingsAccount);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserCredentials().getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        AccountDetailsResponse accountDetailsResponse = AccountDetailsResponse.builder()
                .token(token)
                .currentAccount(currentAccount)
                .savingsAccount(savingsAccount)
                .build();
        log.info("user registered successfully");
        return ResponseEntity.ok(accountDetailsResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody UserRegistrationRequest userRegistrationRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRegistrationRequest.getUsername(), userRegistrationRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(userRegistrationRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);
        log.info("token generated successfully");
        return ResponseEntity.ok(authenticationResponse);
    }
}
