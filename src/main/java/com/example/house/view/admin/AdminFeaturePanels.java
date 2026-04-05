package com.example.house.view.admin;

import com.example.house.controller.admin.AdminController;
import com.example.house.model.dto.admin.AdminDataStore;
import com.example.house.model.enums.AdminRevenuePeriod;
import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import com.example.house.config.ui.UiConstants;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.beans.property.SimpleStringProperty;
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

import java.util.function.Function;

public class AdminFeaturePanels {
    private final AdminController controller;

    public AdminFeaturePanels(AdminController controller) {
        this.controller = controller;
    }

    public Node rateConfigPanel() {
        ComboBox<RateType> typeBox = new ComboBox<>();
        typeBox.getItems().setAll(RateType.values());
        setupEnumCombo(typeBox, AdminFeaturePanels::rateTypeText);
        typeBox.getSelectionModel().selectFirst();

        TextField price = new TextField();
        Label msg = new Label();

        Button save = new Button("Lưu đơn giá");
        save.setOnAction(event -> runSafe(msg, () -> {
            RateType type = required(typeBox.getValue(), "Loại đơn giá");
            controller.saveRate(type, parseMoney(price.getText(), "Đơn giá"));
            msg.setText("Đã cập nhật đơn giá");
        }));

        TableView<AdminDataStore.RateItem> table = new TableView<>(controller.rateConfigs());
        table.getColumns().setAll(
                column("Loại", item -> rateTypeText(item.type()), 160),
                column("Đơn giá", item -> formatMoney(item.unitPrice()), 160)
        );

        VBox box = panel(title("Thiết lập đơn giá"), form(msg, save,
                "Loại", typeBox,
                "Đơn giá", price
        ), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node roomManagementPanel() {
        TextField roomNumber = new TextField();
        ComboBox<Integer> floor = new ComboBox<>();
        TextField size = new TextField();
        TextField baseRent = new TextField();
        TextField furniture = new TextField();

        ObservableList<Integer> floorOptions = FXCollections.observableArrayList();
        refreshFloorOptions(floorOptions);
        floor.setItems(floorOptions);
        floor.setPromptText("Chọn tầng");
        ListChangeListener<AdminDataStore.RoomItem> roomListener = change -> refreshFloorOptions(floorOptions);
        controller.rooms().addListener(roomListener);

        ComboBox<RoomStatus> status = new ComboBox<>();
        status.getItems().setAll(RoomStatus.values());
        setupEnumCombo(status, AdminFeaturePanels::roomStatusText);
        status.getSelectionModel().selectFirst();

        Label msg = new Label();
        TableView<AdminDataStore.RoomItem> table = new TableView<>(controller.rooms());
        table.getColumns().setAll(
                column("Mã", item -> String.valueOf(item.id()), 60),
                column("Phòng", AdminDataStore.RoomItem::roomNumber, 90),
                column("Tầng", item -> String.valueOf(item.floor()), 70),
                column("Giá", item -> formatMoney(item.baseRent()), 120),
                column("Trạng thái", item -> roomStatusText(item.status()), 120)
        );

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, selected) -> {
            if (selected == null) {
                return;
            }
            roomNumber.setText(selected.roomNumber());
            if (selected.floor() != null && !floorOptions.contains(selected.floor())) {
                floorOptions.add(selected.floor());
                FXCollections.sort(floorOptions);
            }
            floor.getSelectionModel().select(selected.floor());
            size.setText(String.valueOf(selected.size()));
            baseRent.setText(String.valueOf(selected.baseRent()));
            furniture.setText(selected.furniture());
            status.getSelectionModel().select(selected.status());
        });

        Button save = new Button("Lưu phòng");
        save.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.RoomItem selected = table.getSelectionModel().getSelectedItem();
            AdminDataStore.RoomItem item = new AdminDataStore.RoomItem(
                    selected == null ? null : selected.id(),
                    required(roomNumber.getText(), "Số phòng"),
                    required(floor.getValue(), "Tầng"),
                    parseMoney(size.getText(), "Diện tích"),
                    parseMoney(baseRent.getText(), "Giá thuê"),
                    furniture.getText(),
                    required(status.getValue(), "Trạng thái")
            );
            controller.saveRoom(item);
            msg.setText("Đã cập nhật phòng");
        }));

        Button delete = new Button("Xóa phòng");
        delete.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.RoomItem selected = requireSelection(table, "Chọn phòng cần xóa");
            controller.deleteRoom(selected.id());
            msg.setText("Đã xóa phòng");
        }));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Số phòng"), roomNumber);
        grid.addRow(1, new Label("Tầng"), floor);
        grid.addRow(2, new Label("Diện tích"), size);
        grid.addRow(3, new Label("Giá thuê"), baseRent);
        grid.addRow(4, new Label("Nội thất"), furniture);
        grid.addRow(5, new Label("Trạng thái"), status);

        VBox box = panel(title("Quản lý tầng / phòng"), grid, new HBox(10, save, delete, msg), table);
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
        Button create = new Button("Tạo tài khoản");
        create.setOnAction(event -> runSafe(msg, () -> {
            controller.createStaffAccount(
                    required(username.getText(), "Tên đăng nhập"),
                    required(password.getText(), "Mật khẩu"),
                    required(fullName.getText(), "Họ tên"),
                    shiftSchedule.getText()
            );
            username.clear();
            password.clear();
            fullName.clear();
            shiftSchedule.clear();
            msg.setText("Đã tạo tài khoản");
        }));

        TableView<AdminDataStore.StaffAccountItem> table = new TableView<>(controller.staffAccounts());
        table.getColumns().setAll(
                column("Mã", item -> String.valueOf(item.id()), 70),
                column("Tên đăng nhập", AdminDataStore.StaffAccountItem::username, 160),
                column("Họ tên", AdminDataStore.StaffAccountItem::fullName, 180),
                column("Ca làm", AdminDataStore.StaffAccountItem::shiftSchedule, 200)
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Tên đăng nhập"), username);
        grid.addRow(1, new Label("Mật khẩu"), password);
        grid.addRow(2, new Label("Họ tên"), fullName);
        grid.addRow(3, new Label("Ca làm"), shiftSchedule);

        VBox box = panel(title("Cấp tài khoản nhân viên"), grid, new HBox(10, create, msg), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node revenueStatsPanel() {
        ComboBox<AdminRevenuePeriod> period = new ComboBox<>();
        period.getItems().setAll(AdminRevenuePeriod.values());
        period.getSelectionModel().select(AdminRevenuePeriod.MONTH);

        TextField year = new TextField(String.valueOf(java.time.Year.now().getValue()));
        TextField periodValue = new TextField();
        Label periodLabel = new Label("Tháng");

        period.valueProperty().addListener((obs, oldValue, selected) -> {
            if (selected == AdminRevenuePeriod.QUARTER) {
                periodLabel.setText("Quý");
                periodValue.setDisable(false);
            } else if (selected == AdminRevenuePeriod.MONTH) {
                periodLabel.setText("Tháng");
                periodValue.setDisable(false);
            } else {
                periodLabel.setText("Giá trị");
                periodValue.clear();
                periodValue.setDisable(true);
            }
        });

        Label msg = new Label();
        Label total = new Label();
        Button refresh = new Button("Thống kê");
        refresh.setOnAction(event -> runSafe(msg, () -> {
            AdminRevenuePeriod selected = required(period.getValue(), "Kỳ thống kê");
            int yearValue = parseInt(year.getText(), "Năm");
            Integer value = periodValue.isDisabled() || periodValue.getText().isBlank()
                    ? null
                    : parseInt(periodValue.getText(), periodLabel.getText());
            controller.refreshRevenue(selected, yearValue, value);
            total.setText("Tổng: " + formatMoney(controller.revenueRows().stream()
                    .mapToDouble(AdminDataStore.RevenueRow::total)
                    .sum()));
            msg.setText("Đã cập nhật báo cáo");
        }));

        TableView<AdminDataStore.RevenueRow> table = new TableView<>(controller.revenueRows());
        table.getColumns().setAll(
                column("Kỳ", AdminDataStore.RevenueRow::label, 140),
                column("Hóa đơn", item -> formatMoney(item.invoiceTotal()), 140),
                column("Bồi thường", item -> formatMoney(item.compensationTotal()), 140),
                column("Tổng", item -> formatMoney(item.total()), 140)
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Kỳ"), period);
        grid.addRow(1, new Label("Năm"), year);
        grid.addRow(2, periodLabel, periodValue);

        VBox box = panel(title("Thống kê doanh thu"), grid, new HBox(10, refresh, msg, total), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node checkoutApprovalPanel() {
        TableView<AdminDataStore.CheckoutItem> table = new TableView<>(controller.pendingCheckouts());
        table.getColumns().setAll(
                column("Hợp đồng", item -> String.valueOf(item.contractId()), 90),
                column("Phòng", AdminDataStore.CheckoutItem::roomNumber, 90),
                column("Khách", AdminDataStore.CheckoutItem::tenantName, 160),
                column("Cọc", item -> formatMoney(item.deposit()), 120),
                column("Bắt đầu", item -> item.startDate() == null ? "" : item.startDate().toString(), 120)
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
        setupEnumCombo(roomStatus, AdminFeaturePanels::roomStatusText);
        roomStatus.getSelectionModel().select(RoomStatus.TRONG);

        ComboBox<CompensationPaymentMethod> paymentMethod = new ComboBox<>();
        paymentMethod.getItems().setAll(CompensationPaymentMethod.values());
        setupEnumCombo(paymentMethod, AdminFeaturePanels::compensationMethodText);
        paymentMethod.getSelectionModel().select(CompensationPaymentMethod.TIEN_MAT);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, selected) -> {
            if (selected == null) {
                return;
            }
            AdminDataStore.CheckoutSummary summary = controller.buildCheckoutSummary(selected.contractId());
            deposit.setText(formatMoney(summary.deposit()));
            unpaidInvoices.setText(formatMoney(summary.unpaidInvoices()));
            unpaidCompensations.setText(formatMoney(summary.unpaidCompensations()));
            refund.setText(formatMoney(summary.refundAmount()));
        });

        Button addCompensation = new Button("Thêm bồi thường");
        addCompensation.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.CheckoutItem selected = requireSelection(table, "Chọn hợp đồng");
            controller.addCompensation(selected.contractId(), parseMoney(compensationAmount.getText(), "Tiền bồi thường"),
                    compensationReason.getText());
            AdminDataStore.CheckoutSummary summary = controller.buildCheckoutSummary(selected.contractId());
            unpaidCompensations.setText(formatMoney(summary.unpaidCompensations()));
            refund.setText(formatMoney(summary.refundAmount()));
            msg.setText("Đã thêm bồi thường");
        }));

        Button approve = new Button("Xác nhận trả phòng");
        approve.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.CheckoutItem selected = requireSelection(table, "Chọn hợp đồng");
            controller.approveCheckout(selected.contractId(), roomStatus.getValue(), paymentMethod.getValue());
            msg.setText("Đã phê duyệt trả phòng");
        }));

        GridPane summaryGrid = new GridPane();
        summaryGrid.setHgap(10);
        summaryGrid.setVgap(10);
        summaryGrid.addRow(0, new Label("Tiền cọc"), deposit);
        summaryGrid.addRow(1, new Label("Hóa đơn nợ"), unpaidInvoices);
        summaryGrid.addRow(2, new Label("Bồi thường"), unpaidCompensations);
        summaryGrid.addRow(3, new Label("Tiền trả"), refund);

        GridPane compensationGrid = new GridPane();
        compensationGrid.setHgap(10);
        compensationGrid.setVgap(10);
        compensationGrid.addRow(0, new Label("Tiền bồi thường"), compensationAmount);
        compensationGrid.addRow(1, new Label("Lý do"), compensationReason);

        GridPane approvalGrid = new GridPane();
        approvalGrid.setHgap(10);
        approvalGrid.setVgap(10);
        approvalGrid.addRow(0, new Label("Trạng thái phòng"), roomStatus);
        approvalGrid.addRow(1, new Label("Hình thức thu"), paymentMethod);

        VBox box = panel(
                title("Phê duyệt trả phòng"),
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
        setupEnumCombo(filter, AdminFeaturePanels::feedbackStatusText);
        filter.getSelectionModel().select(FeedbackStatus.CHO_XU_LY);

        ComboBox<FeedbackStatus> newStatus = new ComboBox<>();
        newStatus.getItems().setAll(FeedbackStatus.DANG_SUA, FeedbackStatus.HOAN_THANH);
        setupEnumCombo(newStatus, AdminFeaturePanels::feedbackStatusText);
        newStatus.getSelectionModel().selectFirst();

        Label msg = new Label();
        Button refresh = new Button("Tải lại");
        refresh.setOnAction(event -> runSafe(msg, () -> {
            controller.refreshAll();
            msg.setText("Đã tải danh sách");
        }));

        TableView<AdminDataStore.FeedbackItem> table = new TableView<>(controller.feedbacks());
        table.getColumns().setAll(
                column("Mã", item -> String.valueOf(item.id()), 60),
                column("Phòng", AdminDataStore.FeedbackItem::roomNumber, 90),
                column("Nội dung", AdminDataStore.FeedbackItem::content, 220),
                column("Trạng thái", item -> feedbackStatusText(item.status()), 120)
        );

        controller.getFeedbacksByStatus(filter.getValue());

        filter.valueProperty().addListener((obs, oldValue, selected) -> {
            controller.getFeedbacksByStatus(selected);
        });

        Button update = new Button("Cập nhật trạng thái");
        update.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.FeedbackItem selected = requireSelection(table, "Chọn phản hồi");
            controller.updateFeedbackStatus(selected.id(), newStatus.getValue());
            msg.setText("Đã cập nhật");
        }));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Lọc"), filter);
        grid.addRow(1, new Label("Trạng thái mới"), newStatus);

        VBox box = panel(title("Quản lý / chỉ đạo phản hồi"), grid, new HBox(10, update, refresh, msg), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node invoiceLookupPanel() {
        TextField roomNumber = new TextField();
        TextField month = new TextField();
        TextField year = new TextField();

        ComboBox<String> paid = new ComboBox<>();
        paid.getItems().setAll("Tất cả", "Đã thanh toán", "Chưa thanh toán");
        paid.getSelectionModel().selectFirst();

        Label msg = new Label();
        Button search = new Button("Tra cứu");
        search.setOnAction(event -> runSafe(msg, () -> {
            Integer monthValue = month.getText().isBlank() ? null : parseInt(month.getText(), "Tháng");
            Integer yearValue = year.getText().isBlank() ? null : parseInt(year.getText(), "Năm");
            Boolean paidValue = switch (paid.getValue()) {
                case "Đã thanh toán" -> Boolean.TRUE;
                case "Chưa thanh toán" -> Boolean.FALSE;
                default -> null;
            };
            controller.searchInvoices(roomNumber.getText(), monthValue, yearValue, paidValue);
            msg.setText("Đã cập nhật danh sách");
        }));

        Button reload = new Button("Tải lại");
        reload.setOnAction(event -> runSafe(msg, () -> {
            controller.reloadInvoices();
            msg.setText("Đã tải lại danh sách");
        }));

        TableView<AdminDataStore.InvoiceItem> table = new TableView<>(controller.invoices());
        table.getColumns().setAll(
                column("Mã", item -> "HD-" + item.id(), 70),
                column("Phòng", AdminDataStore.InvoiceItem::roomNumber, 90),
                column("Tháng", item -> item.month() + "/" + item.year(), 120),
                column("Tổng", item -> formatMoney(item.total()), 120),
                column("Trạng thái", item -> invoiceStatusText(item.paid(), item.paymentMethod()), 140)
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Phòng"), roomNumber);
        grid.addRow(1, new Label("Tháng"), month);
        grid.addRow(2, new Label("Năm"), year);
        grid.addRow(3, new Label("Trạng thái"), paid);

        VBox box = panel(title("Tra cứu hóa đơn"), grid, new HBox(10, search, reload, msg), table);
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
            Label fieldLabel = new Label((String) labelsAndFields[i]);
            fieldLabel.setStyle("-fx-text-fill: #334155; -fx-font-weight: bold;");
            Node field = (Node) labelsAndFields[i + 1];
            grid.addRow(i / 2, fieldLabel, field);
        }

        HBox actions = new HBox(10, action, message);
        VBox box = new VBox(10, grid, actions);
        box.setStyle(UiConstants.CARD);
        return box;
    }

    private static VBox panel(Node... children) {
        VBox box = new VBox(12, children);
        box.setPadding(new Insets(4));
        beautify(box);
        return box;
    }

    private static void beautify(Node node) {
        if (node instanceof TableView<?> table) {
            styleTable(table);
        } else if (node instanceof Button button) {
            button.setStyle(UiConstants.PRIMARY_BUTTON);
        } else if (node instanceof Control control) {
            control.setStyle(UiConstants.INPUT);
            control.setMaxWidth(Double.MAX_VALUE);
        }

        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                beautify(child);
            }
        }
    }

    private static <T> void styleTable(TableView<T> table) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setStyle(UiConstants.TABLE);
    }

    private static Label title(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #1f2937;");
        return label;
    }

    private static <T> T required(T value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " không được để trống");
        }
        if (value instanceof String text && text.isBlank()) {
            throw new IllegalArgumentException(field + " không được để trống");
        }
        return value;
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

    private static String formatMoney(double value) {
        return String.format("%,.0f", value);
    }

    private static String rateTypeText(RateType type) {
        if (type == null) {
            return "";
        }
        return switch (type) {
            case DIEN -> "Điện";
            case NUOC -> "Nước";
            case RAC -> "Rác";
            case XE_MAY -> "Xe máy";
        };
    }

    private static String roomStatusText(RoomStatus status) {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case TRONG -> "Trống";
            case DA_THUE -> "Đã thuê";
            case BAO_TRI -> "Bảo trì";
        };
    }

    private static String feedbackStatusText(FeedbackStatus status) {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case CHO_XU_LY -> "Chờ xử lý";
            case DANG_SUA -> "Đang sửa";
            case HOAN_THANH -> "Hoàn thành";
        };
    }

    private static String invoiceStatusText(boolean paid, String paymentMethod) {
        if (!paid) {
            return "Chưa thanh toán";
        }
        if (paymentMethod == null || paymentMethod.isBlank()) {
            return "Đã thanh toán";
        }
        return switch (paymentMethod) {
            case "TIEN_MAT" -> "Đã thanh toán (Tiền mặt)";
            case "CHUYEN_KHOAN" -> "Đã thanh toán (Chuyển khoản)";
            default -> "Đã thanh toán";
        };
    }

    private static String compensationMethodText(CompensationPaymentMethod method) {
        if (method == null) {
            return "";
        }
        return switch (method) {
            case TIEN_MAT -> "Tiền mặt";
            case CHUYEN_KHOAN -> "Chuyển khoản";
            case TRU_VAO_COC -> "Trừ vào cọc";
        };
    }

    private static <E> void setupEnumCombo(ComboBox<E> comboBox, Function<E, String> mapper) {
        comboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(E item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : mapper.apply(item));
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(E item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : mapper.apply(item));
            }
        });
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

    private void refreshFloorOptions(ObservableList<Integer> floorOptions) {
        floorOptions.setAll(controller.rooms().stream()
                .map(AdminDataStore.RoomItem::floor)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .sorted()
                .toList());
    }
}

