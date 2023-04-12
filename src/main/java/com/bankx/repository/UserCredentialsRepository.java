package com.bankx.repository;

import com.bankx.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {

    UserCredentials findByUsername(String username);
}
