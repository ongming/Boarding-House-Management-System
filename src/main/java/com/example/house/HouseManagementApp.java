package com.example.house;

import com.example.house.controller.auth.LoginController;
import com.example.house.repository.AccountRepository;
import com.example.house.repository.impl.JpaAccountRepository;
import com.example.house.service.AuthService;
import com.example.house.service.impl.AuthServiceImpl;
import com.example.house.view.auth.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HouseManagementApp extends Application {
    private AccountRepository accountRepository;

    public static void launchApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        accountRepository = new JpaAccountRepository();
        AuthService authService = new AuthServiceImpl(accountRepository);
        LoginController loginController = new LoginController(authService);

        LoginView loginView = new LoginView(loginController);
        Scene scene = new Scene(loginView.getRoot(), 500, 420);

        primaryStage.setTitle("Boarding House Management - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (accountRepository != null) {
            accountRepository.close();
        }
    }
}
