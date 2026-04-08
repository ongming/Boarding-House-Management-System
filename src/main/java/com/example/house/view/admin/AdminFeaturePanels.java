package com.example.house.view.admin;

import com.example.house.controller.admin.AdminController;
import com.example.house.model.dto.admin.AdminDataStore;
import com.example.house.model.enums.AdminRevenuePeriod;
import com.example.house.model.enums.CompensationPaymentMethod;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.model.enums.RateType;
import com.example.house.model.enums.RoomStatus;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
import javafx.scene.layout.Region;
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
        styleField(typeBox);

        TextField price = new TextField();
        styleField(price);
        Label msg = statusLabel();

        Button save = new Button("Lưu đơn giá");
        stylePrimaryButton(save);
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
        styleTable(table);

        VBox box = panel(
            sectionHeader("Thiết lập đơn giá", "Thiết lập các mức giá áp dụng cho điện, nước, rác và xe máy."),
            form(msg, save,
                "Loại", typeBox,
                "Đơn giá", price
            ),
            table
        );
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node roomManagementPanel() {
        TextField roomNumber = new TextField();
        ComboBox<Integer> floor = new ComboBox<>();
        TextField size = new TextField();
        TextField baseRent = new TextField();
        TextField furniture = new TextField();
        styleField(roomNumber);
        styleField(floor);
        styleField(size);
        styleField(baseRent);
        styleField(furniture);

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
        styleField(status);

        Label msg = statusLabel();
        TableView<AdminDataStore.RoomItem> table = new TableView<>(controller.rooms());
        table.getColumns().setAll(
                column("Mã", item -> String.valueOf(item.id()), 60),
                column("Phòng", AdminDataStore.RoomItem::roomNumber, 90),
                column("Tầng", item -> String.valueOf(item.floor()), 70),
                column("Giá", item -> formatMoney(item.baseRent()), 120),
                column("Trạng thái", item -> roomStatusText(item.status()), 120)
        );
        styleTable(table);

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
        stylePrimaryButton(save);
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
        styleSecondaryButton(delete);
        delete.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.RoomItem selected = requireSelection(table, "Chọn phòng cần xóa");
            controller.deleteRoom(selected.id());
            msg.setText("Đã xóa phòng");
        }));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        addGridRow(grid, 0, "Số phòng", roomNumber);
        addGridRow(grid, 1, "Tầng", floor);
        addGridRow(grid, 2, "Diện tích", size);
        addGridRow(grid, 3, "Giá thuê", baseRent);
        addGridRow(grid, 4, "Nội thất", furniture);
        addGridRow(grid, 5, "Trạng thái", status);

        VBox box = panel(
                sectionHeader("Quản lý tầng / phòng", "Theo dõi và cập nhật thông tin phòng, giá thuê và trạng thái sử dụng."),
                formCard("Thông tin phòng", new VBox(10, grid, actionRow(msg, save, delete))),
                table
        );
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node staffAccountPanel() {
        TextField username = new TextField();
        TextField password = new TextField();
        TextField fullName = new TextField();
        TextArea shiftSchedule = new TextArea();
        shiftSchedule.setPrefRowCount(2);
        styleField(username);
        styleField(password);
        styleField(fullName);
        styleField(shiftSchedule);

        Label msg = statusLabel();
        Button create = new Button("Tạo tài khoản");
        Button update = new Button("Sửa tài khoản");
        Button delete = new Button("Xóa tài khoản");
        stylePrimaryButton(create);
        styleSecondaryButton(update);
        styleSecondaryButton(delete);

        TableView<AdminDataStore.StaffAccountItem> table = new TableView<>(controller.staffAccounts());
        table.getColumns().setAll(
                column("Mã", item -> String.valueOf(item.id()), 70),
                column("Tên đăng nhập", AdminDataStore.StaffAccountItem::username, 160),
                column("Họ tên", AdminDataStore.StaffAccountItem::fullName, 180),
                column("Ca làm", AdminDataStore.StaffAccountItem::shiftSchedule, 200)
        );
        styleTable(table);

        create.setOnAction(event -> runSafe(msg, () -> {
            controller.createStaffAccount(
                required(username.getText(), "Tên đăng nhập"),
                required(password.getText(), "Mật khẩu"),
                required(fullName.getText(), "Họ tên"),
                shiftSchedule.getText()
            );
            table.getSelectionModel().clearSelection();
            username.clear();
            password.clear();
            fullName.clear();
            shiftSchedule.clear();
            msg.setText("Đã tạo tài khoản");
        }));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, selected) -> {
            if (selected == null) {
                return;
            }
            username.setText(selected.username());
            password.clear();
            fullName.setText(selected.fullName());
            shiftSchedule.setText(selected.shiftSchedule());
        });

        update.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.StaffAccountItem selected = requireSelection(table, "Chọn tài khoản cần sửa");
            controller.updateStaffAccount(
                    selected.id(),
                    required(username.getText(), "Tên đăng nhập"),
                    password.getText(),
                    required(fullName.getText(), "Họ tên"),
                    shiftSchedule.getText()
            );
            msg.setText("Đã cập nhật tài khoản");
        }));

        delete.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.StaffAccountItem selected = requireSelection(table, "Chọn tài khoản cần xóa");
            controller.deleteStaffAccount(selected.id());
            table.getSelectionModel().clearSelection();
            username.clear();
            password.clear();
            fullName.clear();
            shiftSchedule.clear();
            msg.setText("Đã xóa tài khoản");
        }));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        addGridRow(grid, 0, "Tên đăng nhập", username);
        addGridRow(grid, 1, "Mật khẩu", password);
        addGridRow(grid, 2, "Họ tên", fullName);
        addGridRow(grid, 3, "Ca làm", shiftSchedule);

        VBox box = panel(
            sectionHeader("Cấp tài khoản nhân viên", "Tạo tài khoản để nhân viên đăng nhập và thao tác nghiệp vụ."),
            formCard("Thông tin tài khoản", new VBox(10, grid, actionRow(msg, create, update, delete))),
            table
        );
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node revenueStatsPanel() {
        ComboBox<AdminRevenuePeriod> period = new ComboBox<>();
        period.getItems().setAll(AdminRevenuePeriod.values());
        period.getSelectionModel().select(AdminRevenuePeriod.MONTH);
        styleField(period);

        TextField year = new TextField(String.valueOf(java.time.Year.now().getValue()));
        TextField periodValue = new TextField();
        styleField(year);
        styleField(periodValue);
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

        Label msg = statusLabel();
        Label total = new Label();
        Button refresh = new Button("Thống kê");
        stylePrimaryButton(refresh);
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
        styleTable(table);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        addGridRow(grid, 0, "Kỳ", period);
        addGridRow(grid, 1, "Năm", year);
        addGridRow(grid, 2, "Giá trị", periodValue);

        VBox box = panel(
            sectionHeader("Thống kê doanh thu", "Xem báo cáo doanh thu theo tháng, quý hoặc năm."),
            formCard("Bộ lọc thống kê", new VBox(10, grid, actionRow(msg, refresh))),
            totalCard(total),
            table
        );
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
        table.setMinHeight(420);
        table.setPrefHeight(520);
        styleTable(table);

        Label deposit = new Label("0");
        Label unpaidInvoices = new Label("0");
        Label unpaidCompensations = new Label("0");
        Label refund = new Label("0");

        TextField compensationAmount = new TextField();
        TextField compensationReason = new TextField();
        styleField(compensationAmount);
        styleField(compensationReason);
        Label msg = statusLabel();

        ComboBox<RoomStatus> roomStatus = new ComboBox<>();
        roomStatus.getItems().setAll(RoomStatus.values());
        setupEnumCombo(roomStatus, AdminFeaturePanels::roomStatusText);
        roomStatus.getSelectionModel().select(RoomStatus.TRONG);
        styleField(roomStatus);

        ComboBox<CompensationPaymentMethod> paymentMethod = new ComboBox<>();
        paymentMethod.getItems().setAll(CompensationPaymentMethod.values());
        setupEnumCombo(paymentMethod, AdminFeaturePanels::compensationMethodText);
        paymentMethod.getSelectionModel().select(CompensationPaymentMethod.TIEN_MAT);
        styleField(paymentMethod);

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
        styleSecondaryButton(addCompensation);
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
        stylePrimaryButton(approve);
        approve.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.CheckoutItem selected = requireSelection(table, "Chọn hợp đồng");
            controller.approveCheckout(selected.contractId(), roomStatus.getValue(), paymentMethod.getValue());
            msg.setText("Đã phê duyệt trả phòng");
        }));

        GridPane summaryGrid = new GridPane();
        summaryGrid.setHgap(10);
        summaryGrid.setVgap(10);
        addGridRow(summaryGrid, 0, "Tiền cọc", deposit);
        addGridRow(summaryGrid, 1, "Hóa đơn nợ", unpaidInvoices);
        addGridRow(summaryGrid, 2, "Bồi thường", unpaidCompensations);
        addGridRow(summaryGrid, 3, "Tiền trả", refund);

        GridPane compensationGrid = new GridPane();
        compensationGrid.setHgap(10);
        compensationGrid.setVgap(10);
        addGridRow(compensationGrid, 0, "Tiền bồi thường", compensationAmount);
        addGridRow(compensationGrid, 1, "Lý do", compensationReason);

        GridPane approvalGrid = new GridPane();
        approvalGrid.setHgap(10);
        approvalGrid.setVgap(10);
        addGridRow(approvalGrid, 0, "Trạng thái phòng", roomStatus);
        addGridRow(approvalGrid, 1, "Hình thức thu", paymentMethod);

        VBox leftPane = new VBox(10, table);
        leftPane.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        VBox rightPane = new VBox(
                12,
                formCard("Tổng hợp công nợ", summaryGrid),
                formCard("Bồi thường", new VBox(10, compensationGrid, addCompensation)),
                formCard("Phê duyệt", new VBox(10, approvalGrid, actionRow(msg, approve)))
        );
        rightPane.setPrefWidth(380);
        rightPane.setMinWidth(340);

        HBox split = new HBox(16, leftPane, rightPane);
        split.setFillHeight(true);

        VBox box = panel(
                sectionHeader("Phê duyệt trả phòng", "Xem hợp đồng cần trả phòng, tính tiền hoàn và xác nhận kết thúc."),
                split
        );
        VBox.setVgrow(split, Priority.ALWAYS);
        return box;
    }

    public Node feedbackManagementPanel() {
        ComboBox<FeedbackStatus> filter = new ComboBox<>();
        filter.getItems().setAll(FeedbackStatus.values());
        setupEnumCombo(filter, AdminFeaturePanels::feedbackStatusText);
        filter.getSelectionModel().select(FeedbackStatus.CHO_XU_LY);
        styleField(filter);

        ComboBox<FeedbackStatus> newStatus = new ComboBox<>();
        newStatus.getItems().setAll(FeedbackStatus.DANG_SUA, FeedbackStatus.HOAN_THANH);
        setupEnumCombo(newStatus, AdminFeaturePanels::feedbackStatusText);
        newStatus.getSelectionModel().selectFirst();
        styleField(newStatus);

        Label msg = statusLabel();
        Button refresh = new Button("Tải lại");
        styleSecondaryButton(refresh);
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
        styleTable(table);

        controller.getFeedbacksByStatus(filter.getValue());

        filter.valueProperty().addListener((obs, oldValue, selected) -> {
            controller.getFeedbacksByStatus(selected);
        });

        Button update = new Button("Cập nhật trạng thái");
        stylePrimaryButton(update);
        update.setOnAction(event -> runSafe(msg, () -> {
            AdminDataStore.FeedbackItem selected = requireSelection(table, "Chọn phản hồi");
            controller.updateFeedbackStatus(selected.id(), newStatus.getValue());
            msg.setText("Đã cập nhật");
        }));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        addGridRow(grid, 0, "Lọc", filter);
        addGridRow(grid, 1, "Trạng thái mới", newStatus);

        VBox box = panel(
            sectionHeader("Quản lý / chỉ đạo phản hồi", "Theo dõi phản hồi, lọc trạng thái và cập nhật xử lý."),
            formCard("Bộ lọc phản hồi", new VBox(10, grid, actionRow(msg, update, refresh))),
            table
        );
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node invoiceLookupPanel() {
        ComboBox<String> roomNumber = new ComboBox<>();
        ObservableList<String> roomOptions = FXCollections.observableArrayList();
        refreshRoomNumberOptions(roomOptions);
        roomNumber.setItems(roomOptions);
        roomNumber.getSelectionModel().selectFirst();
        styleField(roomNumber);
        ListChangeListener<AdminDataStore.RoomItem> roomListener = change -> refreshRoomNumberOptions(roomOptions);
        controller.rooms().addListener(roomListener);

        TextField month = new TextField();
        TextField year = new TextField();
        styleField(month);
        styleField(year);

        ComboBox<String> paid = new ComboBox<>();
        paid.getItems().setAll("Tất cả", "Đã thanh toán", "Chưa thanh toán");
        paid.getSelectionModel().selectFirst();
        styleField(paid);

        Label msg = statusLabel();
        Button search = new Button("Tra cứu");
        stylePrimaryButton(search);
        search.setOnAction(event -> runSafe(msg, () -> {
            Integer monthValue = month.getText().isBlank() ? null : parseInt(month.getText(), "Tháng");
            Integer yearValue = year.getText().isBlank() ? null : parseInt(year.getText(), "Năm");
            Boolean paidValue = switch (paid.getValue()) {
                case "Đã thanh toán" -> Boolean.TRUE;
                case "Chưa thanh toán" -> Boolean.FALSE;
                default -> null;
            };
            String roomFilter = "Tất cả".equals(roomNumber.getValue()) ? null : roomNumber.getValue();
            controller.searchInvoices(roomFilter, monthValue, yearValue, paidValue);
            msg.setText("Đã cập nhật danh sách");
        }));

        Button reload = new Button("Tải lại");
        styleSecondaryButton(reload);
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
        styleTable(table);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        addGridRow(grid, 0, "Phòng", roomNumber);
        addGridRow(grid, 1, "Tháng", month);
        addGridRow(grid, 2, "Năm", year);
        addGridRow(grid, 3, "Trạng thái", paid);

        VBox box = panel(
            sectionHeader("Tra cứu hóa đơn", "Tìm hóa đơn theo phòng, thời gian và trạng thái thanh toán."),
            formCard("Bộ lọc hóa đơn", new VBox(10, grid, actionRow(msg, search, reload))),
            table
        );
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
            GridPane.setHgrow(field, Priority.ALWAYS);
            if (field instanceof Control control) {
                control.setMaxWidth(Double.MAX_VALUE);
            }
        }

        HBox actions = new HBox(10, action, message);
        VBox box = new VBox(10, grid, actions);
        box.setStyle(cardStyle("#ffffff", "#e2e8f0"));
        box.setPadding(new Insets(16));
        return box;
    }

    private static VBox panel(Node... children) {
        VBox box = new VBox(12, children);
        box.setPadding(new Insets(4));
        return box;
    }

    private static <T> void styleTable(TableView<T> table) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setStyle(tableStyle());
    }

    private static Label title(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        return label;
    }

    private static VBox sectionHeader(String titleText, String subtitleText) {
        Label heading = title(titleText);
        Label subtitle = new Label(subtitleText);
        subtitle.setWrapText(true);
        subtitle.setStyle("-fx-text-fill: #475569; -fx-font-size: 13;");

        VBox header = new VBox(8, heading, subtitle);
        header.setPadding(new Insets(16));
        header.setStyle(cardStyle("linear-gradient(to right, #eff6ff, #f8fafc)", "#bfdbfe"));
        return header;
    }

    private static VBox formCard(String titleText, Node content) {
        Label header = sectionTitle(titleText);
        VBox box = new VBox(10, header, content);
        box.setPadding(new Insets(16));
        box.setStyle(cardStyle("#ffffff", "#e2e8f0"));
        return box;
    }

    private static VBox totalCard(Label total) {
        Label title = sectionTitle("Tổng doanh thu");
        total.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        VBox box = new VBox(8, title, total);
        box.setPadding(new Insets(16));
        box.setStyle(cardStyle("#ffffff", "#e2e8f0"));
        return box;
    }

    private static Label sectionTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 17; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        return label;
    }

    private static HBox actionRow(Label message, Button primary, Button... secondaryButtons) {
        HBox row = new HBox(10);
        row.getChildren().add(primary);
        row.getChildren().addAll(secondaryButtons);
        row.getChildren().add(message);
        HBox.setHgrow(message, Priority.ALWAYS);
        return row;
    }

    private static Label statusLabel() {
        Label message = new Label();
        message.setWrapText(true);
        message.setMinHeight(22);
        return message;
    }

    private static Region growSpacer() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private static void styleField(Control control) {
        control.setStyle(inputFieldStyle());
        control.setMaxWidth(Double.MAX_VALUE);
    }

    private static void stylePrimaryButton(Button button) {
        String normal = "-fx-background-color: linear-gradient(to right, #0ea5e9, #2563eb);"
                + "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 18;";
        String hover = "-fx-background-color: linear-gradient(to right, #0284c7, #1d4ed8);"
                + "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 18;";
        applyHoverStyle(button, normal, hover);
    }

    private static void styleSecondaryButton(Button button) {
        String normal = "-fx-background-color: white; -fx-text-fill: #1e293b; -fx-font-weight: bold;"
                + "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #cbd5e1; -fx-padding: 10 18;";
        String hover = "-fx-background-color: #f8fafc; -fx-text-fill: #0f172a; -fx-font-weight: bold;"
                + "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #94a3b8; -fx-padding: 10 18;";
        applyHoverStyle(button, normal, hover);
    }

    private static void applyHoverStyle(Button button, String normalStyle, String hoverStyle) {
        button.setStyle(normalStyle);
        button.setOnMouseEntered(event -> button.setStyle(hoverStyle));
        button.setOnMouseExited(event -> button.setStyle(normalStyle));
    }

    private static String tableStyle() {
        return "-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 12; -fx-background-radius: 12;";
    }

    private static String cardStyle(String background, String border) {
        return "-fx-background-color: " + background + "; -fx-border-color: " + border + ";"
                + "-fx-border-radius: 14; -fx-background-radius: 14; -fx-effect: dropshadow(gaussian, rgba(15,23,42,0.08), 18, 0.12, 0, 4);";
    }

    private static String inputFieldStyle() {
        return "-fx-background-color: #ffffff; -fx-border-color: #cbd5e1;"
                + "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 10;";
    }

    private static void addGridRow(GridPane grid, int rowIndex, String labelText, Node field) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: #334155; -fx-font-weight: bold;");
        grid.addRow(rowIndex, label, field);
        GridPane.setHgrow(field, Priority.ALWAYS);
        if (field instanceof Control control) {
            control.setMaxWidth(Double.MAX_VALUE);
        }
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

    private void refreshRoomNumberOptions(ObservableList<String> roomOptions) {
        roomOptions.setAll(controller.rooms().stream()
                .map(AdminDataStore.RoomItem::roomNumber)
                .filter(room -> room != null && !room.isBlank())
                .distinct()
                .sorted()
                .toList());
        roomOptions.add(0, "Tất cả");
    }
}

