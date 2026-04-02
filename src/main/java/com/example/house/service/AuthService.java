package com.example.house.service;

import com.example.house.model.entity.Account;

public interface AuthService {
    Account authenticate(String username, String password);

    default Account login(String username, String password) {
        return authenticate(username, password);
    }
}
