package com.example.house.view.staff;

import com.example.house.view.staff.content.DefaultStaffContentFactory;
import com.example.house.view.staff.content.StaffContentFactory;
import com.example.house.view.staff.data.JpaStaffDataStore;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class StaffDashboardView {
    private final BorderPane root;
    private final BorderPane contentPane;
    private final Runnable onLogout;
    private final StaffContentFactory contentFactory;

    public StaffDashboardView(String fullName, Runnable onLogout) {
        this.root = new BorderPane();
        this.contentPane = new BorderPane();
        this.onLogout = onLogout;
        this.contentFactory = new DefaultStaffContentFactory(new JpaStaffDataStore(), this::openContractFromHome);

        root.setStyle("-fx-background-color: #f5f7fa;");
        root.setTop(buildHeader(fullName));
        root.setLeft(buildMenu());

        contentPane.setPadding(new Insets(24));
        contentPane.setStyle("-fx-background-color: #ffffff;");
        contentPane.setCenter(contentFactory.createContent(StaffFeature.HOME));
        root.setCenter(contentPane);
    }

    private VBox buildHeader(String fullName) {
        String displayName = (fullName == null || fullName.isBlank()) ? "Nhân viên" : fullName;

        HBox headerBox = new HBox(12);
        headerBox.setPadding(new Insets(16, 24, 16, 24));
        headerBox.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #3498db); -fx-border-color: #2c3e50; -fx-border-width: 0 0 2 0;");
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Circle avatar = new Circle(20);
        avatar.setFill(Color.web("#3498db"));
        avatar.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 0);");

        VBox titleBox = new VBox(2);
        Label title = new Label("Bảng điều khiển nhân viên");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Xin chào, " + displayName + "!");
        subtitle.setFont(Font.font("Segoe UI", 13));
        subtitle.setTextFill(Color.web("#ecf0f1"));

        titleBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        Button logoutButton = new Button("Đăng xuất");
        logoutButton.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 14 8 14;");
        logoutButton.setOnAction(event -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });

        headerBox.getChildren().addAll(avatar, titleBox, logoutButton);
        return new VBox(headerBox);
    }

    private VBox buildMenu() {
        VBox menuBox = new VBox(8);
        menuBox.setPadding(new Insets(16));
        menuBox.setPrefWidth(280);
        menuBox.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 0 1 0 0;");

        Label menuTitle = new Label("CHỨC NĂNG");
        menuTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        menuTitle.setTextFill(Color.web("#7f8c8d"));

        ListView<StaffFeature> featureList = new ListView<>();
        featureList.setItems(FXCollections.observableArrayList(StaffFeature.values()));
        featureList.setCellFactory(param -> new StaffMenuCell());
        featureList.setPrefHeight(500);
        featureList.setStyle("-fx-padding: 4; -fx-font-size: 12;");

        featureList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) {
                showFeature(selected, null);
            }
        });

        featureList.getSelectionModel().selectFirst();
        menuBox.getChildren().addAll(menuTitle, featureList);
        VBox.setVgrow(featureList, Priority.ALWAYS);
        return menuBox;
    }

    private void showFeature(StaffFeature feature, String context) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(180), contentPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            Node content = contentFactory.createContent(feature, context);
            contentPane.setCenter(content);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(260), contentPane);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    private void openContractFromHome(String roomNumber) {
        showFeature(StaffFeature.CONTRACT, roomNumber);
    }

    public BorderPane getRoot() {
        return root;
    }

    private static class StaffMenuCell extends ListCell<StaffFeature> {
        @Override
        protected void updateItem(StaffFeature item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setStyle("");
                return;
            }

            setText(item.getMenuLabel());
            setFont(Font.font("Segoe UI", 12));
            setPadding(new Insets(8, 12, 8, 12));
            setOnMouseEntered(event -> applyStyle(true, isSelected()));
            setOnMouseExited(event -> applyStyle(false, isSelected()));
            applyStyle(false, isSelected());
        }

        private void applyStyle(boolean hover, boolean selected) {
            if (selected) {
                setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-border-radius: 4; -fx-padding: 8 12 8 12;");
                return;
            }
            if (hover) {
                setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-border-radius: 4; -fx-padding: 8 12 8 12;");
            } else {
                setStyle("-fx-background-color: transparent; -fx-text-fill: #2c3e50; -fx-padding: 8 12 8 12;");
            }
        }
    }
}
