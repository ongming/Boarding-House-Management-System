package com.example.house.controller;

import com.example.house.model.entity.Account;
import com.example.house.service.AuthService;

public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    public Account login(String username, String password) {
        return authService.login(username, password);
    }
}
