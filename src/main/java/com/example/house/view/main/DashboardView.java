package com.example.house.view.main;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class DashboardView {
    private final BorderPane root;

    public DashboardView(String fullName) {
        this.root = new BorderPane();
        this.root.setPadding(new Insets(20));

        Label title = new Label("Boarding House Dashboard");
        title.setFont(Font.font(20));

        Label greeting = new Label("Xin chao, " + fullName + "!");
        greeting.setFont(Font.font(14));

        BorderPane content = new BorderPane();
        content.setTop(title);
        content.setCenter(greeting);

        this.root.setCenter(content);
    }

    public BorderPane getRoot() {
        return root;
    }
}
