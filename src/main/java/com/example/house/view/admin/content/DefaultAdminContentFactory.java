package com.example.house.view.admin.content;

import com.example.house.view.admin.AdminFeature;
import com.example.house.view.admin.data.AdminDataStore;
import com.example.house.view.admin.feature.AdminFeaturePanels;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultAdminContentFactory implements AdminContentFactory {
    private final AdminFeaturePanels panels;
    private final Map<AdminFeature, Supplier<Node>> strategies = new EnumMap<>(AdminFeature.class);

    public DefaultAdminContentFactory(AdminDataStore store) {
        this.panels = new AdminFeaturePanels(store);
        register();
    }

    @Override
    public Node createContent(AdminFeature feature) {
        if (feature == null) {
            return buildOverview();
        }

        Supplier<Node> supplier = strategies.get(feature);
        if (supplier == null) {
            return buildOverview();
        }
        return supplier.get();
    }

    private void register() {
        strategies.put(AdminFeature.OVERVIEW, this::buildOverview);
        strategies.put(AdminFeature.RATE_CONFIG, panels::rateConfigPanel);
        strategies.put(AdminFeature.ROOM_MANAGEMENT, panels::roomManagementPanel);
        strategies.put(AdminFeature.STAFF_ACCOUNT, panels::staffAccountPanel);
        strategies.put(AdminFeature.REVENUE_STATS, panels::revenueStatsPanel);
        strategies.put(AdminFeature.CHECKOUT_APPROVAL, panels::checkoutApprovalPanel);
        strategies.put(AdminFeature.FEEDBACK_MANAGEMENT, panels::feedbackManagementPanel);
        strategies.put(AdminFeature.INVOICE_LOOKUP, panels::invoiceLookupPanel);
    }

    private Node buildOverview() {
        Label title = new Label("Tong quan quan tri");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #1f2937;");

        Label content = new Label("Chon chuc nang o menu trai de cau hinh va giam sat van hanh.");
        content.setWrapText(true);
        content.setStyle("-fx-font-size: 13; -fx-text-fill: #6b7280;");

        return new VBox(12, title, content);
    }
}
