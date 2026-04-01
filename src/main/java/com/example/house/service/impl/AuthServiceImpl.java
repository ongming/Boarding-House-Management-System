package com.example.house.service.impl;

import com.example.house.model.entity.Account;
import com.example.house.repository.AccountRepository;
import com.example.house.service.AuthService;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;

    public AuthServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account login(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Vui long nhap ten dang nhap");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Vui long nhap mat khau");
        }

        Optional<Account> accountOptional = accountRepository.findByUsername(username.trim());
        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Tai khoan khong ton tai");
        }

        Account account = accountOptional.get();
        if (!account.getPassword().equals(password)) {
            throw new IllegalArgumentException("Mat khau khong dung");
        }

        return account;
    }
}
