package com.bankx.repository;

import com.bankx.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t JOIN t.account a WHERE a.id = :accountId")
    List<Transaction> findAllByAccountId(@Param("accountId") Long accountId);

}
