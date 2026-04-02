package com.example.house.view.admin;

import javafx.animation.FadeTransition;
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
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class AdminDashboardView {
    private final BorderPane root;
    private final BorderPane contentPane;
    private final Runnable onLogout;

    public AdminDashboardView(String fullName) {
        this(fullName, null);
    }

    public AdminDashboardView(String fullName, Runnable onLogout) {
        this.root = new BorderPane();
        this.contentPane = new BorderPane();
        this.onLogout = onLogout;

        root.setStyle("-fx-background-color: #f4f6f8;");
        root.setTop(buildHeader(fullName));
        root.setLeft(buildMenu());

        contentPane.setPadding(new Insets(24));
        contentPane.setStyle("-fx-background-color: #ffffff;");
        contentPane.setCenter(buildFeaturePlaceholder(
            "Tổng quan quản trị",
            "Chọn chức năng bên trái để cấu hình hệ thống và giám sát vận hành."
        ));
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

        ListView<String> featureList = new ListView<>();
        featureList.setItems(FXCollections.observableArrayList(
            "⚙ Thiết lập đơn giá",
            "🏢 Quản lý tầng / phòng",
            "👤 Cấp tài khoản nhân viên",
            "📈 Thống kê doanh thu",
            "✅ Phê duyệt trả phòng",
            "🛠 Quản lý / chỉ đạo phản hồi",
            "🧾 Tra cứu hóa đơn"
        ));
        featureList.setCellFactory(param -> new AdminMenuCell());
        featureList.setPrefHeight(560);
        featureList.setStyle("-fx-padding: 4; -fx-font-size: 12;");

        featureList.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, selectedItem) -> {
            if (selectedItem == null) {
                return;
            }
            String featureName = selectedItem.replaceAll("^[^\\s]*\\s+", "");
            updateContent(featureName);
        });

        featureList.getSelectionModel().selectFirst();

        menuBox.getChildren().addAll(menuTitle, featureList);
        VBox.setVgrow(featureList, Priority.ALWAYS);
        return menuBox;
    }

    private void updateContent(String featureName) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(180), contentPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            contentPane.setCenter(buildFeaturePlaceholder(
                featureName,
                "Màn hình này đang ở bản MVP. Bạn có thể nối service/repository tương ứng ở bước tiếp theo."
            ));

            FadeTransition fadeIn = new FadeTransition(Duration.millis(260), contentPane);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    private VBox buildFeaturePlaceholder(String featureName, String description) {
        VBox box = new VBox(14);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-border-color: #dbe2ea; -fx-border-width: 1; -fx-border-radius: 6;");

        Label title = new Label(featureName);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1f2937"));

        Line line = new Line(0, 0, 460, 0);
        line.setStroke(Color.web("#2563eb"));
        line.setStrokeWidth(2);

        Label desc = new Label(description);
        desc.setWrapText(true);
        desc.setLineSpacing(4);
        desc.setFont(Font.font("Segoe UI", 13));
        desc.setTextFill(Color.web("#64748b"));

        box.getChildren().addAll(title, line, desc);
        return box;
    }

    public BorderPane getRoot() {
        return root;
    }

    private static class AdminMenuCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setStyle("");
                return;
            }

            setText(item);
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
