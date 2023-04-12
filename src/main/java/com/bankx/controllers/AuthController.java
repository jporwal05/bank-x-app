package com.bankx.controllers;

import com.bankx.entity.User;
import com.bankx.entity.UserCredentials;
import com.bankx.models.dto.AuthenticationResponse;
import com.bankx.models.dto.UserRegistrationRequest;
import com.bankx.repository.UserRepository;
import com.bankx.security.JwtTokenUtil;
import com.bankx.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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
                .build();
        userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserCredentials().getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody UserRegistrationRequest userRegistrationRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRegistrationRequest.getUsername(), userRegistrationRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(userRegistrationRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);
        return ResponseEntity.ok(authenticationResponse);
    }
}
