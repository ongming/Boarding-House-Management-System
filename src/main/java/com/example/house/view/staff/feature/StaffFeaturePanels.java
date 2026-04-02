package com.example.house.view.staff.feature;

import com.example.house.view.staff.data.StaffDataStore;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.YearMonth;
import java.util.function.Function;

public class StaffFeaturePanels {
    private final StaffDataStore store;

    public StaffFeaturePanels(StaffDataStore store) {
        this.store = store;
    }

    public Node contractPanel() {
        return contractPanel(null);
    }

    public Node contractPanel(String presetRoomCode) {
        TextField room = new TextField();
        if (presetRoomCode != null && !presetRoomCode.isBlank()) {
            room.setText(presetRoomCode);
        }
        TextField tenant = new TextField();
        TextField rent = new TextField();
        TextField deposit = new TextField();
        Label msg = new Label();

        Button save = new Button("Lưu hợp đồng");
        save.setOnAction(event -> runSafe(msg, () -> {
            store.addContract(required(room.getText(), "Phòng"), required(tenant.getText(), "Khách thuê"),
                    parseMoney(rent.getText(), "Tiền phòng"), parseMoney(deposit.getText(), "Tiền cọc"));
            room.clear();
            tenant.clear();
            rent.clear();
            deposit.clear();
            msg.setText("Đã thêm hợp đồng");
        }));

        TableView<StaffDataStore.ContractItem> table = new TableView<>(store.contracts());
        table.getColumns().setAll(
                column("Mã", item -> String.valueOf(item.id()), 60),
                column("Phòng", StaffDataStore.ContractItem::roomCode, 90),
                column("Khách", StaffDataStore.ContractItem::tenantName, 180),
                column("Tiền phòng", item -> formatMoney(item.rentFee()), 120),
                column("Tiền cọc", item -> formatMoney(item.deposit()), 120)
        );

        VBox header = presetRoomCode != null && !presetRoomCode.isBlank()
                ? new VBox(4, title("Lập hợp đồng"), new Label("Đang lập hợp đồng cho phòng " + presetRoomCode))
                : new VBox(0, title("Lập hợp đồng"));

        VBox box = new VBox(12, header, form(msg, save,
                "Phòng", room,
                "Khách thuê", tenant,
                "Tiền phòng", rent,
                "Tiền cọc", deposit
        ), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node vehiclePanel() {
        TextField room = new TextField();
        TextField type = new TextField();
        TextField plate = new TextField();
        TextField fee = new TextField();
        Label msg = new Label();

        Button add = new Button("Thêm xe");
        add.setOnAction(event -> runSafe(msg, () -> {
            store.addVehicle(required(room.getText(), "Phòng"), required(type.getText(), "Loại xe"),
                    required(plate.getText(), "Biển số"), parseMoney(fee.getText(), "Phí xe"));
            room.clear();
            type.clear();
            plate.clear();
            fee.clear();
            msg.setText("Đã thêm xe");
        }));

        TableView<StaffDataStore.VehicleItem> table = new TableView<>(store.vehicles());
        table.getColumns().setAll(
                column("Mã", item -> String.valueOf(item.id()), 60),
                column("Phòng", StaffDataStore.VehicleItem::roomCode, 90),
                column("Loại xe", StaffDataStore.VehicleItem::vehicleType, 130),
                column("Biển số", StaffDataStore.VehicleItem::plateNumber, 140),
                column("Phí/tháng", item -> formatMoney(item.monthlyFee()), 120)
        );

        VBox box = new VBox(12, title("Quản lý xe"), form(msg, add,
                "Phòng", room,
                "Loại xe", type,
                "Biển số", plate,
                "Phí/tháng", fee
        ), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node occupancyPanel() {
        TextField room = new TextField();
        TextField people = new TextField();
        Label msg = new Label();

        Button update = new Button("Cập nhật");
        update.setOnAction(event -> runSafe(msg, () -> {
            store.upsertOccupancy(required(room.getText(), "Phòng"), parseInt(people.getText(), "Số người"));
            room.clear();
            people.clear();
            msg.setText("Đã cập nhật số người ở");
        }));

        TableView<StaffDataStore.OccupancyItem> table = new TableView<>(store.occupancies());
        table.getColumns().setAll(
                column("Phòng", StaffDataStore.OccupancyItem::roomCode, 100),
                column("Số người", item -> String.valueOf(item.peopleCount()), 100),
                column("Cập nhật", item -> item.updatedAt().toString(), 220)
        );

        VBox box = new VBox(12, title("Cập nhật số người ở"), form(msg, update,
                "Phòng", room,
                "Số người", people
        ), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node meterPanel() {
        TextField room = new TextField();
        TextField month = new TextField(YearMonth.now().toString());
        TextField oldElec = new TextField();
        TextField newElec = new TextField();
        TextField oldWater = new TextField();
        TextField newWater = new TextField();
        Label msg = new Label();

        Button save = new Button("Lưu chỉ số");
        save.setOnAction(event -> runSafe(msg, () -> {
            store.addMeterReading(required(room.getText(), "Phòng"), parseMonth(month.getText()),
                    parseInt(oldElec.getText(), "Điện cũ"), parseInt(newElec.getText(), "Điện mới"),
                    parseInt(oldWater.getText(), "Nước cũ"), parseInt(newWater.getText(), "Nước mới"));
            msg.setText("Đã lưu chỉ số điện nước");
        }));

        TableView<StaffDataStore.MeterReadingItem> table = new TableView<>(store.meterReadings());
        table.getColumns().setAll(
                column("Phòng", StaffDataStore.MeterReadingItem::roomCode, 90),
                column("Tháng", item -> item.month().toString(), 90),
                column("Điện", item -> item.oldElectric() + " -> " + item.newElectric(), 130),
                column("Nước", item -> item.oldWater() + " -> " + item.newWater(), 130)
        );

        VBox box = new VBox(12, title("Chốt chỉ số điện nước"), form(msg, save,
                "Phòng", room,
                "Tháng (YYYY-MM)", month,
                "Điện cũ", oldElec,
                "Điện mới", newElec,
                "Nước cũ", oldWater,
                "Nước mới", newWater
        ), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node billingPanel() {
        TextField month = new TextField(YearMonth.now().toString());
        TextField electricRate = new TextField("3500");
        TextField waterRate = new TextField("15000");
        TextField garbage = new TextField("50000");
        Label msg = new Label();

        Button calc = new Button("Tính hóa đơn");
        calc.setOnAction(event -> runSafe(msg, () -> {
            int count = store.generateInvoices(parseMonth(month.getText()), parseMoney(electricRate.getText(), "Đơn giá điện"),
                    parseMoney(waterRate.getText(), "Đơn giá nước"), parseMoney(garbage.getText(), "Phí rác"));
            msg.setText("Đã tạo " + count + " hóa đơn");
        }));

        TableView<StaffDataStore.InvoiceItem> table = invoiceTable(store.invoices());
        VBox box = new VBox(12, title("Tính toán / Xuất hóa đơn"), form(msg, calc,
                "Tháng (YYYY-MM)", month,
                "Đơn giá điện", electricRate,
                "Đơn giá nước", waterRate,
                "Phí rác", garbage
        ), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node paymentPanel() {
        ComboBox<StaffDataStore.InvoiceItem> invoiceBox = new ComboBox<>();
        invoiceBox.setItems(store.invoices().filtered(item -> !item.paid()));
        invoiceBox.setCellFactory(param -> invoiceCell());
        invoiceBox.setButtonCell(invoiceCell());

        ComboBox<String> method = new ComboBox<>();
        method.getItems().addAll("Tiền mặt", "Chuyển khoản");
        method.getSelectionModel().selectFirst();

        Label msg = new Label();
        Button pay = new Button("Xác nhận đã thu");
        pay.setOnAction(event -> runSafe(msg, () -> {
            StaffDataStore.InvoiceItem selected = invoiceBox.getValue();
            if (selected == null) {
                throw new IllegalArgumentException("Chọn hóa đơn cần thanh toán");
            }
            store.markInvoicePaid(selected.id(), method.getValue());
            invoiceBox.setItems(store.invoices().filtered(item -> !item.paid()));
            msg.setText("Đã cập nhật trạng thái thanh toán");
        }));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Hóa đơn"), invoiceBox);
        grid.addRow(1, new Label("Phương thức"), method);
        grid.addRow(2, pay, msg);

        TableView<StaffDataStore.InvoiceItem> table = invoiceTable(store.invoices());
        VBox box = new VBox(12, title("Xác nhận thanh toán"), grid, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node invoiceLookupPanel() {
        TextField filter = new TextField();
        filter.setPromptText("Lọc theo mã phòng...");

        FilteredList<StaffDataStore.InvoiceItem> filtered = new FilteredList<>(store.invoices(), item -> true);
        filter.textProperty().addListener((obs, oldValue, value) -> {
            String key = value == null ? "" : value.trim().toLowerCase();
            filtered.setPredicate(item -> key.isEmpty() || item.roomCode().toLowerCase().contains(key));
        });

        TableView<StaffDataStore.InvoiceItem> table = invoiceTable(filtered);
        VBox box = new VBox(12, title("Xem hóa đơn"), filter, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node feedbackPanel() {
        TextField room = new TextField();
        TextField title = new TextField();
        TextArea description = new TextArea();
        description.setPrefRowCount(3);

        ComboBox<String> priority = new ComboBox<>();
        priority.getItems().addAll("Thấp", "Trung bình", "Cao", "Khẩn cấp");
        priority.getSelectionModel().select(1);

        Label msg = new Label();
        Button add = new Button("Ghi nhận");
        add.setOnAction(event -> runSafe(msg, () -> {
            store.addFeedback(required(room.getText(), "Phòng"), required(title.getText(), "Tiêu đề"),
                    required(description.getText(), "Nội dung"), priority.getValue());
            room.clear();
            title.clear();
            description.clear();
            msg.setText("Đã ghi nhận sự cố");
        }));

        TableView<StaffDataStore.FeedbackItem> table = new TableView<>(store.feedbacks());
        table.getColumns().setAll(
                column("Mã", item -> String.valueOf(item.id()), 60),
                column("Phòng", StaffDataStore.FeedbackItem::roomCode, 90),
                column("Tiêu đề", StaffDataStore.FeedbackItem::title, 180),
                column("Mức độ", StaffDataStore.FeedbackItem::priority, 120),
                column("Trạng thái", StaffDataStore.FeedbackItem::status, 120)
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Phòng"), room);
        grid.addRow(1, new Label("Tiêu đề"), title);
        grid.addRow(2, new Label("Nội dung"), description);
        grid.addRow(3, new Label("Mức độ"), priority);

        VBox box = new VBox(12, title("Ghi nhận sự cố / Phản hồi"), grid, new HBox(10, add, msg), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    private ListCell<StaffDataStore.InvoiceItem> invoiceCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(StaffDataStore.InvoiceItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "HD-" + item.id() + " | " + item.roomCode() + " | " + formatMoney(item.totalAmount()));
            }
        };
    }

    private TableView<StaffDataStore.InvoiceItem> invoiceTable(ObservableList<StaffDataStore.InvoiceItem> source) {
        TableView<StaffDataStore.InvoiceItem> table = new TableView<>(source);
        table.getColumns().setAll(
                column("Mã", item -> "HD-" + item.id(), 70),
                column("Phòng", StaffDataStore.InvoiceItem::roomCode, 90),
                column("Tháng", item -> item.month().toString(), 100),
                column("Tổng tiền", item -> formatMoney(item.totalAmount()), 120),
                column("Trạng thái", item -> item.paid() ? item.paymentMethod() : "Chưa thanh toán", 150)
        );
        return table;
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

    private static <T> TableColumn<T, String> column(String title, Function<T, String> extractor, double width) {
        TableColumn<T, String> col = new TableColumn<>(title);
        col.setCellValueFactory(data -> new SimpleStringProperty(extractor.apply(data.getValue())));
        col.setPrefWidth(width);
        return col;
    }

    private static String required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " không được để trống");
        }
        return value.trim();
    }

    private static int parseInt(String value, String field) {
        try {
            return Integer.parseInt(required(value, field));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(field + " phải là số nguyên");
        }
    }

    private static double parseMoney(String value, String field) {
        try {
            return Double.parseDouble(required(value, field));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(field + " phải là số");
        }
    }

    private static YearMonth parseMonth(String value) {
        try {
            return YearMonth.parse(required(value, "Tháng"));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Tháng phải theo định dạng YYYY-MM");
        }
    }

    private static String formatMoney(double value) {
        return String.format("%,.0f", value);
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

