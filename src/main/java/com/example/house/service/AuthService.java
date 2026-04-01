package com.example.house.service;

import com.example.house.model.entity.Account;

public interface AuthService {
    Account login(String username, String password);
}
