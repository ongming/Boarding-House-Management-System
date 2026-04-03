package com.example.house.repository;

import com.example.house.model.entity.Account;

import java.util.Optional;

public interface AccountRepository extends AutoCloseable {
    Optional<Account> findByUsername(String username);

    Account save(Account account);

    @Override
    default void close() {
        // No-op by default for repositories that do not keep resources.
    }
}
