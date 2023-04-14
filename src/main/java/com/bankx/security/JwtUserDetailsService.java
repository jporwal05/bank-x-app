package com.bankx.security;

import com.bankx.entity.UserCredentials;
import com.bankx.repository.UserCredentialsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserCredentialsRepository userCredentialsRepository;

    @Autowired
    public JwtUserDetailsService(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentials userCredentials = userCredentialsRepository.findByUsername(username);

        if (userCredentials == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new JwtUserDetails(userCredentials.getUsername(), userCredentials.getPassword());
    }
}
