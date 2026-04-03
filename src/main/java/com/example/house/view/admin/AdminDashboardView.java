package com.example.house.view.admin;

import javafx.animation.FadeTransition;
import com.example.house.view.admin.content.AdminContentFactory;
import com.example.house.view.admin.content.DefaultAdminContentFactory;
import com.example.house.view.admin.data.JpaAdminDataStore;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

public class AdminDashboardView {
    private final BorderPane root;
    private final BorderPane contentPane;
    private final Runnable onLogout;
    private final AdminContentFactory contentFactory;

    public AdminDashboardView(String fullName) {
        this(fullName, null);
    }

    public AdminDashboardView(String fullName, Runnable onLogout) {
        this.root = new BorderPane();
        this.contentPane = new BorderPane();
        this.onLogout = onLogout;
        this.contentFactory = new DefaultAdminContentFactory(new JpaAdminDataStore());

        root.setStyle("-fx-background-color: #f4f6f8;");
        root.setTop(buildHeader(fullName));
        root.setLeft(buildMenu());

        contentPane.setPadding(new Insets(24));
        contentPane.setStyle("-fx-background-color: #ffffff;");
        contentPane.setCenter(contentFactory.createContent(AdminFeature.OVERVIEW));
        root.setCenter(contentPane);
    }

    private HBox buildHeader(String fullName) {
        String displayName = (fullName == null || fullName.isBlank()) ? "Quản trị viên" : fullName;

        HBox header = new HBox(12);
        header.setPadding(new Insets(16, 24, 16, 24));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: linear-gradient(to right, #1e293b, #2563eb);"
                + "-fx-border-color: #1e293b; -fx-border-width: 0 0 2 0;");

        Circle avatar = new Circle(20);
        avatar.setFill(Color.web("#60a5fa"));
        avatar.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.35), 6, 0, 0, 1);");

        VBox textBox = new VBox(2);
        Label title = new Label("Bảng điều khiển quản trị");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Xin chào, " + displayName + "!");
        subtitle.setFont(Font.font("Segoe UI", 13));
        subtitle.setTextFill(Color.web("#dbeafe"));

        textBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Button logoutButton = new Button("Đăng xuất");
        logoutButton.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;"
                + "-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 6;"
                + "-fx-padding: 8 14 8 14;");
        logoutButton.setOnAction(event -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });

        header.getChildren().addAll(avatar, textBox, logoutButton);
        return header;
    }

    private VBox buildMenu() {
        VBox menuBox = new VBox(8);
        menuBox.setPadding(new Insets(16));
        menuBox.setPrefWidth(320);
        menuBox.setStyle("-fx-background-color: #e2e8f0; -fx-border-color: #cbd5e1; -fx-border-width: 0 1 0 0;");

        Label menuTitle = new Label("CHỨC NĂNG ADMIN");
        menuTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        menuTitle.setTextFill(Color.web("#475569"));

        ListView<AdminFeature> featureList = new ListView<>();
        featureList.setItems(FXCollections.observableArrayList(AdminFeature.values()));
        featureList.setCellFactory(param -> new AdminMenuCell());
        featureList.setPrefHeight(560);
        featureList.setStyle("-fx-padding: 4; -fx-font-size: 12;");

        featureList.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, selectedItem) -> {
            if (selectedItem == null) {
                return;
            }
            updateContent(selectedItem);
        });

        featureList.getSelectionModel().selectFirst();

        menuBox.getChildren().addAll(menuTitle, featureList);
        VBox.setVgrow(featureList, Priority.ALWAYS);
        return menuBox;
    }

    private void updateContent(AdminFeature feature) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(180), contentPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            contentPane.setCenter(contentFactory.createContent(feature));

            FadeTransition fadeIn = new FadeTransition(Duration.millis(260), contentPane);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }


    public BorderPane getRoot() {
        return root;
    }

    private static class AdminMenuCell extends ListCell<AdminFeature> {
        @Override
        protected void updateItem(AdminFeature item, boolean empty) {
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

            setOnMouseEntered(event -> setMenuStyle(true, isSelected()));
            setOnMouseExited(event -> setMenuStyle(false, isSelected()));
            setMenuStyle(false, isSelected());
        }

        private void setMenuStyle(boolean hover, boolean selected) {
            if (selected) {
                setStyle("-fx-background-color: #1d4ed8; -fx-text-fill: white;"
                        + "-fx-border-radius: 4; -fx-padding: 8 12 8 12;");
                return;
            }

            if (hover) {
                setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white;"
                        + "-fx-border-radius: 4; -fx-padding: 8 12 8 12;");
            } else {
                setStyle("-fx-background-color: transparent; -fx-text-fill: #1f2937;"
                        + "-fx-padding: 8 12 8 12;");
            }
        }
    }
}
