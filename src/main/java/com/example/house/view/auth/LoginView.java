package com.example.house.view.auth;

import com.example.house.config.ViewComposition;
import com.example.house.controller.auth.LoginController;
import com.example.house.model.entity.Account;
import com.example.house.model.enums.AccountType;
import com.example.house.config.ui.UiConstants;
import com.example.house.config.ui.VietnameseTextNormalizer;
import com.example.house.view.main.DashboardView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {
    private final VBox root;
    private final LoginController loginController;
    private final ViewComposition composition;

    public LoginView(LoginController loginController) {
        this.loginController = loginController;
        this.composition = new ViewComposition();
        this.root = new VBox(UiConstants.SPACING);
        initialize();
    }

    private void initialize() {
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f5f7fa, #ecf0f1);");

        Label title = new Label("Đăng nhập hệ thống");
        title.setFont(javafx.scene.text.Font.font("Segoe UI", javafx.scene.text.FontWeight.BOLD, 26));
        title.setTextFill(javafx.scene.paint.Color.web("#2c3e50"));

        Label subtitle = new Label("Quản lý nhà trọ hiện đại");
        subtitle.setFont(javafx.scene.text.Font.font("Segoe UI", 13));
        subtitle.setTextFill(javafx.scene.paint.Color.web("#7f8c8d"));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Tên đăng nhập");
        usernameField.setMaxWidth(UiConstants.FORM_WIDTH);
        usernameField.setStyle("-fx-padding: 10; -fx-font-size: 13; -fx-border-radius: 4; "
                + "-fx-border-color: #bdc3c7; -fx-font-family: 'Segoe UI';");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mật khẩu");
        passwordField.setMaxWidth(UiConstants.FORM_WIDTH);
        passwordField.setStyle("-fx-padding: 10; -fx-font-size: 13; -fx-border-radius: 4; "
                + "-fx-border-color: #bdc3c7; -fx-font-family: 'Segoe UI';");

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Mật khẩu");
        visiblePasswordField.setMaxWidth(UiConstants.FORM_WIDTH);
        visiblePasswordField.setStyle(passwordField.getStyle());
        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);

        CheckBox showPasswordCheckBox = new CheckBox("Hiện mật khẩu");
        showPasswordCheckBox.setStyle("-fx-font-family: 'Segoe UI'; -fx-text-fill: #64748b; -fx-font-size: 12;");
        showPasswordCheckBox.selectedProperty().addListener((obs, oldValue, show) -> {
            passwordField.setVisible(!show);
            passwordField.setManaged(!show);
            visiblePasswordField.setVisible(show);
            visiblePasswordField.setManaged(show);
        });

        Label messageLabel = new Label();
        messageLabel.setTextFill(javafx.scene.paint.Color.web("#e74c3c"));
        messageLabel.setWrapText(true);
        messageLabel.setFont(javafx.scene.text.Font.font("Segoe UI", 12));

        Button loginButton = new Button("ĐĂNG NHẬP");
        loginButton.setDefaultButton(true);
        loginButton.setMaxWidth(UiConstants.FORM_WIDTH);
        loginButton.setPrefHeight(42);
        loginButton.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10; "
                + "-fx-background-color: linear-gradient(to right, #3498db, #2980b9); "
                + "-fx-text-fill: white; -fx-border-radius: 4; "
                + "-fx-font-family: 'Segoe UI'; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");

        loginButton.setOnMouseEntered(event -> {
            loginButton.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10; "
                    + "-fx-background-color: linear-gradient(to right, #2980b9, #2471a3); "
                    + "-fx-text-fill: white; -fx-border-radius: 4; "
                    + "-fx-font-family: 'Segoe UI'; "
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 8, 0, 0, 3);");
        });

        loginButton.setOnMouseExited(event -> {
            loginButton.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 10; "
                    + "-fx-background-color: linear-gradient(to right, #3498db, #2980b9); "
                    + "-fx-text-fill: white; -fx-border-radius: 4; "
                    + "-fx-font-family: 'Segoe UI'; "
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        });

        loginButton.setOnAction(event -> {
            try {
                String password = showPasswordCheckBox.isSelected()
                        ? visiblePasswordField.getText()
                        : passwordField.getText();
                Account account = loginController.login(usernameField.getText(), password);
                openDashboard(account);
            } catch (IllegalArgumentException ex) {
                messageLabel.setText(ex.getMessage());
            }
        });

        VBox formBox = new VBox(12);
        formBox.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-border-radius: 8; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 5);");
        formBox.setAlignment(Pos.CENTER);
        formBox.getChildren().addAll(
                title,
                subtitle,
                usernameField,
                passwordField,
                visiblePasswordField,
                showPasswordCheckBox,
                loginButton,
                messageLabel
        );

        root.getChildren().add(formBox);
        VietnameseTextNormalizer.normalizeNodeTree(root);
    }

    private void openDashboard(Account account) {
        Stage stage = (Stage) root.getScene().getWindow();
        Runnable onLogout = () -> showLoginOnStage(stage);

        if (account.getAccountType() == AccountType.NHAN_VIEN) {
            var staffDashboardView = composition.buildStaffDashboard(account.getFullName(), onLogout);
            stage.setTitle("Bảng điều khiển nhân viên");
            stage.setScene(new Scene(staffDashboardView.getRoot(), 980, 620));
        } else if (account.getAccountType() == AccountType.ADMIN) {
            var adminDashboardView = composition.buildAdminDashboard(account.getFullName(), onLogout);
            stage.setTitle("Bảng điều khiển quản trị");
            stage.setScene(new Scene(adminDashboardView.getRoot(), 1080, 680));
        } else {
            DashboardView dashboardView = new DashboardView(account.getFullName());
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(dashboardView.getRoot(), 700, 480));
        }

        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }

    private void showLoginOnStage(Stage stage) {
        LoginView loginView = new LoginView(loginController);
        stage.setTitle("Boarding House Management - Login");
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.setScene(new Scene(loginView.getRoot(), 500, 420));
        stage.centerOnScreen();
        stage.show();
    }

    public Parent getRoot() {
        return root;
    }
}
