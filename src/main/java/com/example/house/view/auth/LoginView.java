package com.example.house.view.auth;

import com.example.house.controller.LoginController;
import com.example.house.model.entity.Account;
import com.example.house.view.common.UiConstants;
import com.example.house.view.main.DashboardView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginView {
    private final VBox root;
    private final LoginController loginController;

    public LoginView(LoginController loginController) {
        this.loginController = loginController;
        this.root = new VBox(UiConstants.SPACING);
        initialize();
    }

    private void initialize() {
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Dang nhap he thong");
        title.setFont(Font.font(22));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Ten dang nhap");
        usernameField.setMaxWidth(UiConstants.FORM_WIDTH);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mat khau");
        passwordField.setMaxWidth(UiConstants.FORM_WIDTH);

        Label messageLabel = new Label();

        Button loginButton = new Button("Dang nhap");
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(event -> {
            try {
                Account account = loginController.login(usernameField.getText(), passwordField.getText());
                openDashboard(account.getFullName());
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            } catch (IllegalArgumentException ex) {
                messageLabel.setText(ex.getMessage());
            }
        });

        root.getChildren().addAll(title, usernameField, passwordField, loginButton, messageLabel);
    }

    private void openDashboard(String fullName) {
        DashboardView dashboardView = new DashboardView(fullName);
        Stage dashboardStage = new Stage();
        dashboardStage.setTitle("Dashboard");
        dashboardStage.setScene(new Scene(dashboardView.getRoot(), 700, 480));
        dashboardStage.show();
    }

    public Parent getRoot() {
        return root;
    }
}
