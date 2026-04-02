package com.example.house.view.staff.content;

import com.example.house.view.staff.StaffFeature;
import com.example.house.view.staff.data.StaffDataStore;
import com.example.house.view.staff.feature.StaffFeaturePanels;
import com.example.house.view.staff.home.StaffHomeCatalog;
import com.example.house.view.staff.home.StaffHomePanel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultStaffContentFactory implements StaffContentFactory {
    private final StaffFeaturePanels panels;
    private final StaffHomePanel homePanel;
    private final Map<StaffFeature, Function<String, Node>> strategies = new EnumMap<>(StaffFeature.class);

    public DefaultStaffContentFactory(StaffDataStore store) {
        this(store, null);
    }

    public DefaultStaffContentFactory(StaffDataStore store, java.util.function.Consumer<String> onCreateContract) {
        this.panels = new StaffFeaturePanels(store);
        this.homePanel = new StaffHomePanel(new StaffHomeCatalog(), onCreateContract);
        registerStrategies();
    }

    @Override
    public Node createContent(StaffFeature feature) {
        return createContent(feature, null);
    }

    @Override
    public Node createContent(StaffFeature feature, String context) {
        if (feature == null) {
            return buildOverview();
        }

        Function<String, Node> supplier = strategies.get(feature);
        if (supplier == null) {
            return buildOverview();
        }
        return supplier.apply(context);
    }

    private void registerStrategies() {
        strategies.put(StaffFeature.HOME, context -> homePanel.getRoot());
        strategies.put(StaffFeature.CONTRACT, panels::contractPanel);
        strategies.put(StaffFeature.VEHICLE, context -> panels.vehiclePanel());
        strategies.put(StaffFeature.OCCUPANCY, context -> panels.occupancyPanel());
        strategies.put(StaffFeature.METER_READING, context -> panels.meterPanel());
        strategies.put(StaffFeature.BILLING, context -> panels.billingPanel());
        strategies.put(StaffFeature.PAYMENT, context -> panels.paymentPanel());
        strategies.put(StaffFeature.INVOICE_LOOKUP, context -> panels.invoiceLookupPanel());
        strategies.put(StaffFeature.FEEDBACK, context -> panels.feedbackPanel());
    }

    private Node buildOverview() {
        Label title = new Label("Tổng quan nhân viên");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label content = new Label("Chọn chức năng ở menu trái để thao tác. Dữ liệu được lưu tạm trong phiên làm việc hiện tại.");
        content.setWrapText(true);
        content.setStyle("-fx-font-size: 13; -fx-text-fill: #6b7280;");

        return new VBox(12, title, content);
    }
}
