package com.example.house.view.admin.feature;

import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import com.example.house.service.admin.AdminRevenuePeriod;
import com.example.house.view.admin.data.AdminDataStore;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.Function;

public class AdminFeaturePanels {
    private final AdminDataStore store;

    public AdminFeaturePanels(AdminDataStore store) {
        this.store = store;
    }

    public Node rateConfigPanel() {
        ComboBox<RateType> typeBox = new ComboBox<>();
        typeBox.getItems().setAll(RateType.values());
        typeBox.getSelectionModel().selectFirst();

        TextField price = new TextField();
        Label msg = new Label();

        Button save = new Button("Luu don gia");
        save.setOnAction(event -> runSafe(msg, () -> {
            RateType type = required(typeBox.getValue(), "Loai don gia");
            store.saveRate(type, parseMoney(price.getText(), "Don gia"));
            msg.setText("Da cap nhat don gia");
        }));

        TableView<AdminDataStore.RateItem> table = new TableView<>(store.rateConfigs());
        table.getColumns().setAll(
                column("Loai", item -> item.type().name(), 160),
                column("Don gia", item -> formatMoney(item.unitPrice()), 160)
        );

        VBox box = new VBox(12, title("Thiet lap don gia"), form(msg, save,
                "Loai", typeBox,
                "Don gia", price
        ), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node roomManagementPanel() {
        TextField roomNumber = new TextField();
        TextField floor = new TextField();
        TextField size = new TextField();
        TextField baseRent = new TextField();
        TextField furniture = new TextField();

        ComboBox<RoomStatus> status = new ComboBox<>();
        status.getItems().setAll(RoomStatus.values());
        status.getSelectionModel().selectFirst();

        Label msg = new Label();
        TableView<AdminDataStore.RoomItem> table = new TableView<>(store.rooms());
        table.getColumns().setAll(
                column("Ma", item -> String.valueOf(item.id()), 60),
                column("Phong", AdminDataStore.RoomItem::roomNumber, 90),
                column("Tang", item -> String.valueOf(item.floor()), 70),
                column("Gia", item -> formatMoney(item.baseRent()), 120),
                column("Trang thai", item -> item.status().name(), 120)
        );

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, selected) -> {
            if (selected == null) {
                return;
            }
            roomNumber.setText(selected.roomNumber());
            floor.setText(selected.floor() == null ? "" : selected.floor().toString());
            size.setText(String.valueOf(selected.size()));
            baseRent.setText(String.valueOf(selected.baseRent()));
            furniture.setText(selected.furniture());
            status.getSelectionModel().select(selected.status());
        });

        Button save = new Button("Luu phong");
        save.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.RoomItem selected = table.getSelectionModel().getSelectedItem();
            AdminDataStore.RoomItem item = new AdminDataStore.RoomItem(
                    selected == null ? null : selected.id(),
                    required(roomNumber.getText(), "So phong"),
                    parseInt(floor.getText(), "Tang"),
                    parseMoney(size.getText(), "Dien tich"),
                    parseMoney(baseRent.getText(), "Gia thue"),
                    furniture.getText(),
                    required(status.getValue(), "Trang thai")
            );
            store.saveRoom(item);
            msg.setText("Da cap nhat phong");
        }));

        Button delete = new Button("Xoa phong");
        delete.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.RoomItem selected = requireSelection(table, "Chon phong can xoa");
            store.deleteRoom(selected.id());
            msg.setText("Da xoa phong");
        }));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("So phong"), roomNumber);
        grid.addRow(1, new Label("Tang"), floor);
        grid.addRow(2, new Label("Dien tich"), size);
        grid.addRow(3, new Label("Gia thue"), baseRent);
        grid.addRow(4, new Label("Noi that"), furniture);
        grid.addRow(5, new Label("Trang thai"), status);

        VBox box = new VBox(12, title("Quan ly tang / phong"), grid, new HBox(10, save, delete, msg), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node staffAccountPanel() {
        TextField username = new TextField();
        TextField password = new TextField();
        TextField fullName = new TextField();
        TextArea shiftSchedule = new TextArea();
        shiftSchedule.setPrefRowCount(2);

        Label msg = new Label();
        Button create = new Button("Tao tai khoan");
        create.setOnAction(event -> runSafe(msg, () -> {
            store.createStaffAccount(
                    required(username.getText(), "Ten dang nhap"),
                    required(password.getText(), "Mat khau"),
                    required(fullName.getText(), "Ho ten"),
                    shiftSchedule.getText()
            );
            username.clear();
            password.clear();
            fullName.clear();
            shiftSchedule.clear();
            msg.setText("Da tao tai khoan");
        }));

        TableView<AdminDataStore.StaffAccountItem> table = new TableView<>(store.staffAccounts());
        table.getColumns().setAll(
                column("Ma", item -> String.valueOf(item.id()), 70),
                column("Username", AdminDataStore.StaffAccountItem::username, 160),
                column("Ho ten", AdminDataStore.StaffAccountItem::fullName, 180),
                column("Ca lam", AdminDataStore.StaffAccountItem::shiftSchedule, 200)
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Ten dang nhap"), username);
        grid.addRow(1, new Label("Mat khau"), password);
        grid.addRow(2, new Label("Ho ten"), fullName);
        grid.addRow(3, new Label("Ca lam"), shiftSchedule);

        VBox box = new VBox(12, title("Cap tai khoan nhan vien"), grid, new HBox(10, create, msg), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node revenueStatsPanel() {
        ComboBox<AdminRevenuePeriod> period = new ComboBox<>();
        period.getItems().setAll(AdminRevenuePeriod.values());
        period.getSelectionModel().select(AdminRevenuePeriod.MONTH);

        TextField year = new TextField(String.valueOf(java.time.Year.now().getValue()));
        TextField periodValue = new TextField();
        Label periodLabel = new Label("Thang");

        period.valueProperty().addListener((obs, oldValue, selected) -> {
            if (selected == AdminRevenuePeriod.QUARTER) {
                periodLabel.setText("Quy");
                periodValue.setDisable(false);
            } else if (selected == AdminRevenuePeriod.MONTH) {
                periodLabel.setText("Thang");
                periodValue.setDisable(false);
            } else {
                periodLabel.setText("Gia tri");
                periodValue.clear();
                periodValue.setDisable(true);
            }
        });

        Label msg = new Label();
        Label total = new Label();
        Button refresh = new Button("Thong ke");
        refresh.setOnAction(event -> runSafe(msg, () -> {
            AdminRevenuePeriod selected = required(period.getValue(), "Ky thong ke");
            int yearValue = parseInt(year.getText(), "Nam");
            Integer value = periodValue.isDisabled() || periodValue.getText().isBlank()
                    ? null
                    : parseInt(periodValue.getText(), periodLabel.getText());
            store.refreshRevenue(selected, yearValue, value);
            total.setText("Tong: " + formatMoney(store.revenueRows().stream()
                    .mapToDouble(AdminDataStore.RevenueRow::total)
                    .sum()));
            msg.setText("Da cap nhat bao cao");
        }));

        TableView<AdminDataStore.RevenueRow> table = new TableView<>(store.revenueRows());
        table.getColumns().setAll(
                column("Ky", AdminDataStore.RevenueRow::label, 140),
                column("Hoa don", item -> formatMoney(item.invoiceTotal()), 140),
                column("Boi thuong", item -> formatMoney(item.compensationTotal()), 140),
                column("Tong", item -> formatMoney(item.total()), 140)
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Ky"), period);
        grid.addRow(1, new Label("Nam"), year);
        grid.addRow(2, periodLabel, periodValue);

        VBox box = new VBox(12, title("Thong ke doanh thu"), grid, new HBox(10, refresh, msg, total), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node checkoutApprovalPanel() {
        TableView<AdminDataStore.CheckoutItem> table = new TableView<>(store.pendingCheckouts());
        table.getColumns().setAll(
                column("Hop dong", item -> String.valueOf(item.contractId()), 90),
                column("Phong", AdminDataStore.CheckoutItem::roomNumber, 90),
                column("Khach", AdminDataStore.CheckoutItem::tenantName, 160),
                column("Coc", item -> formatMoney(item.deposit()), 120),
                column("Bat dau", item -> item.startDate() == null ? "" : item.startDate().toString(), 120)
        );

        Label deposit = new Label("0");
        Label unpaidInvoices = new Label("0");
        Label unpaidCompensations = new Label("0");
        Label refund = new Label("0");

        TextField compensationAmount = new TextField();
        TextField compensationReason = new TextField();
        Label msg = new Label();

        ComboBox<RoomStatus> roomStatus = new ComboBox<>();
        roomStatus.getItems().setAll(RoomStatus.values());
        roomStatus.getSelectionModel().select(RoomStatus.TRONG);

        ComboBox<CompensationPaymentMethod> paymentMethod = new ComboBox<>();
        paymentMethod.getItems().setAll(CompensationPaymentMethod.values());
        paymentMethod.getSelectionModel().select(CompensationPaymentMethod.TIEN_MAT);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, selected) -> {
            if (selected == null) {
                return;
            }
            AdminDataStore.CheckoutSummary summary = store.buildCheckoutSummary(selected.contractId());
            deposit.setText(formatMoney(summary.deposit()));
            unpaidInvoices.setText(formatMoney(summary.unpaidInvoices()));
            unpaidCompensations.setText(formatMoney(summary.unpaidCompensations()));
            refund.setText(formatMoney(summary.refundAmount()));
        });

        Button addCompensation = new Button("Them boi thuong");
        addCompensation.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.CheckoutItem selected = requireSelection(table, "Chon hop dong");
            store.addCompensation(selected.contractId(), parseMoney(compensationAmount.getText(), "Tien boi thuong"),
                    compensationReason.getText());
            AdminDataStore.CheckoutSummary summary = store.buildCheckoutSummary(selected.contractId());
            unpaidCompensations.setText(formatMoney(summary.unpaidCompensations()));
            refund.setText(formatMoney(summary.refundAmount()));
            msg.setText("Da them boi thuong");
        }));

        Button approve = new Button("Xac nhan tra phong");
        approve.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.CheckoutItem selected = requireSelection(table, "Chon hop dong");
            store.approveCheckout(selected.contractId(), roomStatus.getValue(), paymentMethod.getValue());
            msg.setText("Da phe duyet tra phong");
        }));

        GridPane summaryGrid = new GridPane();
        summaryGrid.setHgap(10);
        summaryGrid.setVgap(10);
        summaryGrid.addRow(0, new Label("Tien coc"), deposit);
        summaryGrid.addRow(1, new Label("Hoa don no"), unpaidInvoices);
        summaryGrid.addRow(2, new Label("Boi thuong"), unpaidCompensations);
        summaryGrid.addRow(3, new Label("Tien tra"), refund);

        GridPane compensationGrid = new GridPane();
        compensationGrid.setHgap(10);
        compensationGrid.setVgap(10);
        compensationGrid.addRow(0, new Label("Tien boi thuong"), compensationAmount);
        compensationGrid.addRow(1, new Label("Ly do"), compensationReason);

        GridPane approvalGrid = new GridPane();
        approvalGrid.setHgap(10);
        approvalGrid.setVgap(10);
        approvalGrid.addRow(0, new Label("Trang thai phong"), roomStatus);
        approvalGrid.addRow(1, new Label("Hinh thuc thu"), paymentMethod);

        VBox box = new VBox(12,
                title("Phe duyet tra phong"),
                table,
                new HBox(16, summaryGrid, new VBox(10, compensationGrid, addCompensation)),
                approvalGrid,
                new HBox(10, approve, msg)
        );
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node feedbackManagementPanel() {
        ComboBox<FeedbackStatus> filter = new ComboBox<>();
        filter.getItems().setAll(FeedbackStatus.values());
        filter.getSelectionModel().select(FeedbackStatus.CHO_XU_LY);

        ComboBox<FeedbackStatus> newStatus = new ComboBox<>();
        newStatus.getItems().setAll(FeedbackStatus.DANG_SUA, FeedbackStatus.HOAN_THANH);
        newStatus.getSelectionModel().selectFirst();

        Label msg = new Label();
        Button refresh = new Button("Tai lai");
        refresh.setOnAction(event -> runSafe(msg, () -> {
            store.refreshAll();
            msg.setText("Da tai danh sach");
        }));

        TableView<AdminDataStore.FeedbackItem> table = new TableView<>(store.feedbacks());
        table.getColumns().setAll(
                column("Ma", item -> String.valueOf(item.id()), 60),
                column("Phong", AdminDataStore.FeedbackItem::roomNumber, 90),
                column("Noi dung", AdminDataStore.FeedbackItem::content, 220),
                column("Trang thai", item -> item.status().name(), 120)
        );

        store.getFeedbacksByStatus(filter.getValue());

        filter.valueProperty().addListener((obs, oldValue, selected) -> {
            store.getFeedbacksByStatus(selected);
        });

        Button update = new Button("Cap nhat trang thai");
        update.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.FeedbackItem selected = requireSelection(table, "Chon phan hoi");
            store.updateFeedbackStatus(selected.id(), newStatus.getValue());
            msg.setText("Da cap nhat");
        }));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Loc"), filter);
        grid.addRow(1, new Label("Trang thai moi"), newStatus);

        VBox box = new VBox(12, title("Quan ly / chi dao phan hoi"), grid, new HBox(10, update, refresh, msg), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node invoiceLookupPanel() {
        TextField roomNumber = new TextField();
        TextField month = new TextField();
        TextField year = new TextField();

        ComboBox<String> paid = new ComboBox<>();
        paid.getItems().setAll("Tat ca", "Da thanh toan", "Chua thanh toan");
        paid.getSelectionModel().selectFirst();

        Label msg = new Label();
        Button search = new Button("Tra cuu");
        search.setOnAction(event -> runSafe(msg, () -> {
            Integer monthValue = month.getText().isBlank() ? null : parseInt(month.getText(), "Thang");
            Integer yearValue = year.getText().isBlank() ? null : parseInt(year.getText(), "Nam");
            Boolean paidValue = switch (paid.getValue()) {
                case "Da thanh toan" -> Boolean.TRUE;
                case "Chua thanh toan" -> Boolean.FALSE;
                default -> null;
            };
            store.searchInvoices(roomNumber.getText(), monthValue, yearValue, paidValue);
            msg.setText("Da cap nhat danh sach");
        }));

        Button reload = new Button("Tai lai");
        reload.setOnAction(event -> runSafe(msg, () -> {
            store.reloadInvoices();
            msg.setText("Da tai lai danh sach");
        }));

        TableView<AdminDataStore.InvoiceItem> table = new TableView<>(store.invoices());
        table.getColumns().setAll(
                column("Ma", item -> "HD-" + item.id(), 70),
                column("Phong", AdminDataStore.InvoiceItem::roomNumber, 90),
                column("Thang", item -> item.month() + "/" + item.year(), 120),
                column("Tong", item -> formatMoney(item.total()), 120),
                column("Trang thai", item -> item.paid() ? item.paymentMethod() : "CHUA_THU", 140)
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Phong"), roomNumber);
        grid.addRow(1, new Label("Thang"), month);
        grid.addRow(2, new Label("Nam"), year);
        grid.addRow(3, new Label("Trang thai"), paid);

        VBox box = new VBox(12, title("Tra cuu hoa don"), grid, new HBox(10, search, reload, msg), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    private static <T> TableColumn<T, String> column(String title, Function<T, String> extractor, double width) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> new SimpleStringProperty(extractor.apply(data.getValue())));
        column.setPrefWidth(width);
        return column;
    }

    private static VBox form(Label message, Button action, Object... labelsAndFields) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < labelsAndFields.length; i += 2) {
            grid.addRow(i / 2, new Label((String) labelsAndFields[i]), (Node) labelsAndFields[i + 1]);
        }

        return new VBox(10, grid, new HBox(10, action, message));
    }

    private static Label title(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #1f2937;");
        return label;
    }

    private static <T> T required(T value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " khong duoc de trong");
        }
        if (value instanceof String text && text.isBlank()) {
            throw new IllegalArgumentException(field + " khong duoc de trong");
        }
        return value;
    }

    private static int parseInt(String value, String field) {
        try {
            return Integer.parseInt(required(value, field));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(field + " phai la so nguyen");
        }
    }

    private static double parseMoney(String value, String field) {
        try {
            return Double.parseDouble(required(value, field));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(field + " phai la so");
        }
    }

    private static String formatMoney(double value) {
        return String.format("%,.0f", value);
    }

    private static <T> T requireSelection(TableView<T> table, String msg) {
        T selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            throw new IllegalArgumentException(msg);
        }
        return selected;
    }

    private static void runSafe(Label message, Runnable action) {
        try {
            action.run();
            message.setStyle("-fx-text-fill: #16a34a;");
        } catch (IllegalArgumentException ex) {
            message.setText(ex.getMessage());
            message.setStyle("-fx-text-fill: #dc2626;");
        }
    }
}
