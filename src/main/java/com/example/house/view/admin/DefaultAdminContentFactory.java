package com.example.house.view.admin;

import com.example.house.controller.admin.AdminController;
import com.example.house.model.enums.AdminFeature;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DefaultAdminContentFactory implements AdminContentFactory {
    private final AdminFeaturePanels panels;

    public DefaultAdminContentFactory(AdminController controller) {
        this.panels = new AdminFeaturePanels(controller);
    }

    @Override
    public Node createContent(AdminFeature feature) {
        if (feature == null) {
            return buildOverview();
        }

        return switch (feature) {
            case OVERVIEW -> buildOverview();
            case RATE_CONFIG -> panels.rateConfigPanel();
            case ROOM_MANAGEMENT -> panels.roomManagementPanel();
            case STAFF_ACCOUNT -> panels.staffAccountPanel();
            case REVENUE_STATS -> panels.revenueStatsPanel();
            case CHECKOUT_APPROVAL -> panels.checkoutApprovalPanel();
            case FEEDBACK_MANAGEMENT -> panels.feedbackManagementPanel();
            case INVOICE_LOOKUP -> panels.invoiceLookupPanel();
        };
    }

    private Node buildOverview() {
        Label title = new Label("Tổng quan quản trị");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #1f2937;");

        Label content = new Label("Chọn chức năng ở menu trái để cấu hình và giám sát vận hành.");
        content.setWrapText(true);
        content.setStyle("-fx-font-size: 13; -fx-text-fill: #6b7280;");

        return new VBox(12, title, content);
    }
}
