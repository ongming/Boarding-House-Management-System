package com.example.house.view.staff;

import com.example.house.controller.staff.StaffFeatureController;
import com.example.house.controller.staff.home.StaffHomeCatalog;
import com.example.house.model.enums.StaffFeature;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class DefaultStaffContentFactory implements StaffContentFactory {
    private final StaffFeaturePanels panels;
    private final StaffHomeCatalog homeCatalog;
    private final Consumer<String> onCreateContract;
    private Consumer<String> onContractCreated;

    public DefaultStaffContentFactory(StaffFeatureController controller, StaffHomeCatalog homeCatalog) {
        this(controller, homeCatalog, null);
    }

    public DefaultStaffContentFactory(StaffFeatureController controller,
                                      StaffHomeCatalog homeCatalog,
                                      Consumer<String> onCreateContract) {
        this.panels = new StaffFeaturePanels(controller, homeCatalog);
        this.homeCatalog = homeCatalog;
        this.onCreateContract = onCreateContract;
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

        return switch (feature) {
            case HOME -> {
                homeCatalog.refresh();
                yield new StaffHomePanel(homeCatalog, onCreateContract).getRoot();
            }
            case CONTRACT -> panels.contractPanel(context, roomCode -> {
                homeCatalog.refresh();
                if (onContractCreated != null) {
                    onContractCreated.accept(roomCode);
                }
            });
            case VEHICLE -> panels.vehiclePanel();
            case OCCUPANCY -> panels.occupancyPanel();
            case METER_READING -> panels.meterPanel();
            case BILLING -> panels.billingPanel();
            case PAYMENT -> panels.paymentPanel();
            case INVOICE_LOOKUP -> panels.invoiceLookupPanel();
            case FEEDBACK -> panels.feedbackPanel();
        };
    }

    private Node buildOverview() {
        Label title = new Label("Tổng quan nhân viên");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label content = new Label("Chọn chức năng ở menu trái để thao tác.");
        content.setWrapText(true);
        content.setStyle("-fx-font-size: 13; -fx-text-fill: #6b7280;");

        return new VBox(12, title, content);
    }

    public void setOnContractCreated(Consumer<String> onContractCreated) {
        this.onContractCreated = onContractCreated;
    }
}
