package com.bankx.security;

import com.bankx.entity.UserCredentials;
import com.bankx.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JwtUserDetailsService(UserCredentialsRepository userCredentialsRepository, PasswordEncoder passwordEncoder) {
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentials userCredentials = userCredentialsRepository.findByUsername(username);

        if (userCredentials == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new JwtUserDetails(userCredentials.getUsername(), userCredentials.getPassword());
    }

    public void saveUserCredentials(UserCredentials userCredentials) {
        String encodedPassword = passwordEncoder.encode(userCredentials.getPassword());
        userCredentials.setPassword(encodedPassword);
        userCredentialsRepository.save(userCredentials);
    }
}
