package com.example.house.view.staff;

import com.example.house.controller.staff.StaffFeatureController;
import com.example.house.controller.staff.home.StaffHomeCatalog;
import com.example.house.service.staff.StaffDataStore;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class StaffFeaturePanels {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    private final StaffFeatureController controller;
    private final StaffHomeCatalog homeCatalog;

    public StaffFeaturePanels(StaffFeatureController controller, StaffHomeCatalog homeCatalog) {
        this.controller = controller;
        this.homeCatalog = homeCatalog;
    }

    public StaffFeaturePanels(StaffFeatureController controller) {
        this(controller, null);
    }

    public Node contractPanel(String presetRoomCode, Consumer<String> onContractCreated) {
        boolean roomPreset = presetRoomCode != null && !presetRoomCode.isBlank();

        Label pageTitle = title("Lập hợp đồng thuê phòng");
        Label subtitle = new Label(roomPreset
                ? "Đang lập hợp đồng cho phòng " + presetRoomCode + ". Nhập đầy đủ thông tin khách thuê và thông tin hợp đồng bên dưới."
                : "Bạn hãy chọn phòng ở Trang chủ và nhấn 'Lập hợp đồng' để mở đúng biểu mẫu.");
        subtitle.setWrapText(true);
        subtitle.setStyle("-fx-text-fill: #475569; -fx-font-size: 13;");

        if (!roomPreset) {
            Label hint = new Label("Chức năng này được mở từ Trang chủ để đảm bảo đúng phòng cần lập hợp đồng.");
            hint.setWrapText(true);
            hint.setStyle("-fx-text-fill: #b45309; -fx-font-size: 13;");

            VBox noticeBox = new VBox(10, pageTitle, subtitle, hint);
            noticeBox.setPadding(new Insets(18));
            noticeBox.setStyle(cardStyle("#fffbeb", "#fde68a"));
            return noticeBox;
        }

        TextField room = new TextField(presetRoomCode);
        room.setEditable(false);
        room.setFocusTraversable(false);
        styleField(room);
        room.setStyle(inputFieldStyle() + "-fx-background-color: #eef2ff; -fx-text-fill: #1e293b;");

        TextField tenantName = new TextField();
        TextField tenantCccd = new TextField();
        TextField tenantPhone = new TextField();
        DatePicker startDate = new DatePicker(LocalDate.now());
        DatePicker moveInDate = new DatePicker(LocalDate.now());
        DatePicker endDate = new DatePicker();
        TextField occupantCount = new TextField("1");
        TextField rent = new TextField();
        String baseRentText = resolveBaseRentText(presetRoomCode);
        if (!baseRentText.isBlank()) {
            rent.setText(baseRentText);
        }
        TextField deposit = new TextField();
        TextField contractImagePath = new TextField();
        Button browseImage = new Button("Chọn ảnh...");
        Label msg = new Label();
        msg.setWrapText(true);
        msg.setMinHeight(22);

        styleField(tenantName);
        styleField(tenantCccd);
        styleField(tenantPhone);
        styleField(startDate);
        styleField(moveInDate);
        styleField(endDate);
        styleField(occupantCount);
        styleField(rent);
        styleField(deposit);
        styleField(contractImagePath);
        startDate.setMaxWidth(Double.MAX_VALUE);
        moveInDate.setMaxWidth(Double.MAX_VALUE);
        endDate.setMaxWidth(Double.MAX_VALUE);

        contractImagePath.setEditable(false);
        contractImagePath.setFocusTraversable(false);
        contractImagePath.setPromptText("Chưa chọn ảnh hợp đồng");
        styleSecondaryButton(browseImage);

        browseImage.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Chọn ảnh hợp đồng");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Ảnh hợp đồng", "*.png", "*.jpg", "*.jpeg", "*.webp"),
                    new FileChooser.ExtensionFilter("Tất cả file", "*.*")
            );
            File selected = chooser.showOpenDialog(browseImage.getScene() == null ? null : browseImage.getScene().getWindow());
            if (selected != null) {
                contractImagePath.setText(selected.getAbsolutePath());
            }
        });

        tenantName.setPromptText("Nhập họ tên đầy đủ của khách thuê");
        tenantCccd.setPromptText("Nhập số CCCD/CMND");
        tenantPhone.setPromptText("Nhập số điện thoại");
        startDate.setPromptText("Chọn ngày bắt đầu");
        moveInDate.setPromptText("Chọn ngày dọn vào");
        endDate.setPromptText("Chọn ngày kết thúc (nếu có)");
        occupantCount.setPromptText("Ví dụ: 2");
        rent.setPromptText("Ví dụ: 3.000.000");
        deposit.setPromptText("Ví dụ: 3.000.000");

        Runnable clearForm = () -> {
            tenantName.clear();
            tenantCccd.clear();
            tenantPhone.clear();
            startDate.setValue(LocalDate.now());
            moveInDate.setValue(LocalDate.now());
            endDate.setValue(null);
            occupantCount.setText("1");
            if (baseRentText.isBlank()) {
                rent.clear();
            } else {
                rent.setText(baseRentText);
            }
            deposit.clear();
            contractImagePath.clear();
        };

        Button save = new Button("Lưu hợp đồng");
        Button updateMoveIn = new Button("Cập nhật ngày dọn vào");
        Button reset = new Button("Làm mới");
        stylePrimaryButton(save);
        styleSecondaryButton(updateMoveIn);
        styleSecondaryButton(reset);

        TableView<StaffDataStore.ContractItem> table = new TableView<>(controller.contracts());
        table.getColumns().setAll(List.of(
                column("Mã HĐ", item -> "HD-" + item.id(), 80),
                column("Phòng", StaffDataStore.ContractItem::roomCode, 90),
                column("Khách thuê", StaffDataStore.ContractItem::tenantName, 160),
                column("Ngày bắt đầu", item -> formatDate(item.startDate()), 110),
                column("Ngày dọn vào", item -> formatDate(item.moveInDate()), 110),
                column("Ngày kết thúc", item -> formatDate(item.endDate()), 110),
                column("Tiền phòng", item -> formatMoney(item.rentFee()), 120),
                column("Tiền cọc", item -> formatMoney(item.deposit()), 120),
                column("Ảnh HĐ", item -> shortPath(item.contractImageUrl()), 140),
                column("Tạo lúc", item -> formatDateTime(item.createdAt()), 160)
        ));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setStyle(tableStyle());
        table.setPrefHeight(280);
        table.setMinHeight(220);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, selected) -> {
            if (selected == null) {
                return;
            }
            startDate.setValue(selected.startDate());
            moveInDate.setValue(selected.moveInDate() != null ? selected.moveInDate() : selected.startDate());
            endDate.setValue(selected.endDate());
        });

        save.setOnAction(event -> runSafe(msg, () -> {
            controller.createContract(
                    room.getText(),
                    tenantName.getText(),
                    tenantCccd.getText(),
                    tenantPhone.getText(),
                    startDate.getValue(),
                    moveInDate.getValue(),
                    endDate.getValue(),
                    contractImagePath.getText(),
                    occupantCount.getText(),
                    rent.getText(),
                    deposit.getText()
            );

            // Ensure UI list updates immediately before switching view.
            table.setItems(controller.contracts());
            table.refresh();

            clearForm.run();
            msg.setText("Đã lập hợp đồng thành công");

            if (onContractCreated != null) {
                onContractCreated.accept(room.getText());
            }
        }));

        updateMoveIn.setOnAction(event -> runSafe(msg, () -> {
            StaffDataStore.ContractItem selected = requireSelection(table, "Chọn hợp đồng cần cập nhật");
            LocalDate moveIn = requiredDate(moveInDate.getValue(), "Ngày dọn vào");
            controller.updateContractMoveInDate(selected.id(), moveIn);
            table.setItems(controller.contracts());
            table.refresh();
            msg.setText("Đã cập nhật ngày dọn vào");
        }));

        reset.setOnAction(event -> {
            clearForm.run();
            msg.setText("Đã làm mới biểu mẫu");
            msg.setStyle("-fx-text-fill: #2563eb;");
        });

        HBox imageRow = new HBox(8, contractImagePath, browseImage);
        HBox.setHgrow(contractImagePath, Priority.ALWAYS);

        VBox customerCard = fieldCard("1. Thông tin khách thuê",
                "Họ và tên", tenantName,
                "CCCD/CMND", tenantCccd,
                "Số điện thoại", tenantPhone
        );
        VBox contractCard = fieldCard("2. Thông tin hợp đồng",
                "Phòng", room,
                "Ngày bắt đầu", startDate,
                "Ngày dọn vào", moveInDate,
                "Ngày kết thúc", endDate,
                "Ảnh hợp đồng", imageRow,
                "Số người ở", occupantCount,
                "Tiền phòng/tháng", rent,
                "Tiền cọc", deposit
        );
        customerCard.setMaxWidth(Double.MAX_VALUE);
        contractCard.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(customerCard, Priority.ALWAYS);
        VBox.setVgrow(contractCard, Priority.ALWAYS);

        HBox cards = new HBox(16, customerCard, contractCard);
        cards.setFillHeight(true);

        HBox actions = new HBox(10, save, updateMoveIn, reset, msg);
        actions.setPadding(new Insets(4, 0, 0, 0));
        HBox.setHgrow(msg, Priority.ALWAYS);

        Label tableTitle = new Label("Danh sách hợp đồng");
        tableTitle.setStyle("-fx-font-size: 17; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        VBox header = new VBox(8, pageTitle, subtitle);
        header.setPadding(new Insets(16));
        header.setStyle(cardStyle("linear-gradient(to right, #eff6ff, #f8fafc)", "#bfdbfe"));

        VBox box = new VBox(16, header, cards, actions, tableTitle, table);
        box.setPadding(new Insets(4));
        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scrollPane;
    }

    public Node vehiclePanel() {
        ComboBox<String> room = new ComboBox<>(controller.roomCodes());
        TextField type = new TextField();
        TextField plate = new TextField();
        TextField fee = new TextField();
        Label msg = statusLabel();

        styleField(room);
        styleField(type);
        styleField(plate);
        styleField(fee);
        room.setPromptText("Chọn phòng");
        type.setPromptText("Ví dụ: Xe máy");
        plate.setPromptText("Ví dụ: 59X1-123.45");
        fee.setPromptText("Ví dụ: 120000");

        Button add = new Button("Thêm xe");
        Button clear = new Button("Làm mới");
        stylePrimaryButton(add);
        styleSecondaryButton(clear);

        add.setOnAction(event -> runSafe(msg, () -> {
            controller.addVehicle(room.getValue(), type.getText(), plate.getText(), fee.getText());
            room.getSelectionModel().clearSelection();
            type.clear();
            plate.clear();
            fee.clear();
            msg.setText("Đã thêm xe thành công");
        }));

        clear.setOnAction(event -> {
            room.getSelectionModel().clearSelection();
            type.clear();
            plate.clear();
            fee.clear();
            msg.setText("Đã làm mới biểu mẫu");
            msg.setStyle("-fx-text-fill: #2563eb;");
        });

        VBox formCard = fieldCard("Thông tin xe",
                "Phòng", room,
                "Loại xe", type,
                "Biển số", plate,
                "Phí/tháng", fee
        );

        TableView<StaffDataStore.VehicleItem> table = new TableView<>(controller.vehicles());
        table.getColumns().setAll(List.of(
                column("Mã", item -> String.valueOf(item.id()), 70),
                column("Phòng", StaffDataStore.VehicleItem::roomCode, 90),
                column("Loại xe", StaffDataStore.VehicleItem::vehicleType, 150),
                column("Biển số", StaffDataStore.VehicleItem::plateNumber, 160),
                column("Phí/tháng", item -> formatMoney(item.monthlyFee()), 120)
        ));
        styleTable(table);

        VBox box = new VBox(16,
                sectionHeader("Quản lý phương tiện", "Thêm và cập nhật xe của khách theo từng phòng."),
                formCard,
                actionRow(msg, add, clear),
                sectionTitle("Danh sách phương tiện"),
                table,
                growSpacer()
        );
        box.setPadding(new Insets(4));
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node occupancyPanel() {
        ComboBox<String> room = new ComboBox<>(controller.roomCodes());
        TextField people = new TextField();
        Label msg = statusLabel();

        styleField(room);
        styleField(people);
        room.setPromptText("Chọn phòng");
        people.setPromptText("Ví dụ: 3");

        Button update = new Button("Cập nhật");
        Button clear = new Button("Làm mới");
        stylePrimaryButton(update);
        styleSecondaryButton(clear);

        update.setOnAction(event -> runSafe(msg, () -> {
            controller.updateOccupancy(room.getValue(), people.getText());
            room.getSelectionModel().clearSelection();
            people.clear();
            msg.setText("Đã cập nhật số người ở");
        }));

        clear.setOnAction(event -> {
            room.getSelectionModel().clearSelection();
            people.clear();
            msg.setText("Đã làm mới biểu mẫu");
            msg.setStyle("-fx-text-fill: #2563eb;");
        });

        VBox formCard = fieldCard("Cập nhật lưu trú",
                "Phòng", room,
                "Số người", people
        );

        TableView<StaffDataStore.OccupancyItem> table = new TableView<>(controller.occupancies());
        table.getColumns().setAll(List.of(
                column("Phòng", StaffDataStore.OccupancyItem::roomCode, 120),
                column("Số người", item -> String.valueOf(item.peopleCount()), 120),
                column("Cập nhật lúc", item -> formatDateTime(item.updatedAt()), 190)
        ));
        styleTable(table);

        VBox box = new VBox(16,
                sectionHeader("Quản lý số người ở", "Theo dõi và cập nhật số người đang lưu trú theo từng phòng."),
                formCard,
                actionRow(msg, update, clear),
                sectionTitle("Tình trạng lưu trú"),
                table,
                growSpacer()
        );
        box.setPadding(new Insets(4));
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node meterPanel() {
        ComboBox<String> room = new ComboBox<>(controller.roomCodes());
        TextField month = new TextField(YearMonth.now().toString());
        TextField oldElec = new TextField();
        TextField newElec = new TextField();
        TextField oldWater = new TextField();
        TextField newWater = new TextField();
        Label msg = statusLabel();

        styleField(room);
        styleField(month);
        styleField(oldElec);
        styleField(newElec);
        styleField(oldWater);
        styleField(newWater);

        room.setPromptText("Chọn phòng");
        month.setPromptText("YYYY-MM");

        Button save = new Button("Lưu chỉ số");
        Button clear = new Button("Làm mới");
        stylePrimaryButton(save);
        styleSecondaryButton(clear);

        save.setOnAction(event -> runSafe(msg, () -> {
            controller.saveMeterReading(
                    room.getValue(),
                    month.getText(),
                    oldElec.getText(),
                    newElec.getText(),
                    oldWater.getText(),
                    newWater.getText()
            );
            msg.setText("Đã lưu chỉ số điện nước");
        }));

        clear.setOnAction(event -> {
            room.getSelectionModel().clearSelection();
            month.setText(YearMonth.now().toString());
            oldElec.clear();
            newElec.clear();
            oldWater.clear();
            newWater.clear();
            msg.setText("Đã làm mới biểu mẫu");
            msg.setStyle("-fx-text-fill: #2563eb;");
        });

        VBox formCard = fieldCard("Nhập chỉ số điện nước",
                "Phòng", room,
                "Tháng (YYYY-MM)", month,
                "Điện cũ", oldElec,
                "Điện mới", newElec,
                "Nước cũ", oldWater,
                "Nước mới", newWater
        );

        TableView<StaffDataStore.MeterReadingItem> table = new TableView<>(controller.meterReadings());
        table.getColumns().setAll(List.of(
                column("Phòng", StaffDataStore.MeterReadingItem::roomCode, 90),
                column("Tháng", item -> item.month().toString(), 100),
                column("Điện", item -> item.oldElectric() + " -> " + item.newElectric(), 160),
                column("Nước", item -> item.oldWater() + " -> " + item.newWater(), 160)
        ));
        styleTable(table);

        VBox box = new VBox(16,
                sectionHeader("Chốt chỉ số điện nước", "Lưu chỉ số định kỳ để phục vụ tính hóa đơn."),
                formCard,
                actionRow(msg, save, clear),
                sectionTitle("Lịch sử chỉ số"),
                table,
                growSpacer()
        );
        box.setPadding(new Insets(4));
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node billingPanel() {
        TextField month = new TextField(YearMonth.now().toString());
        TextField electricRate = new TextField("3500");
        TextField waterRate = new TextField("15000");
        TextField garbage = new TextField("50000");
        Label msg = statusLabel();

        styleField(month);
        styleField(electricRate);
        styleField(waterRate);
        styleField(garbage);

        Button calc = new Button("Tính hóa đơn");
        stylePrimaryButton(calc);

        calc.setOnAction(event -> runSafe(msg, () -> {
            int count = controller.generateInvoices(
                    month.getText(),
                    electricRate.getText(),
                    waterRate.getText(),
                    garbage.getText()
            );
            msg.setText("Đã tạo " + count + " hóa đơn");
        }));

        VBox formCard = fieldCard("Thiết lập tính tiền",
                "Tháng (YYYY-MM)", month,
                "Đơn giá điện", electricRate,
                "Đơn giá nước", waterRate,
                "Phí rác", garbage
        );

        TableView<StaffDataStore.InvoiceItem> table = invoiceTable(controller.invoices());
        VBox box = new VBox(16,
                sectionHeader("Tính toán / Xuất hóa đơn", "Tính tự động hóa đơn dựa trên chỉ số điện nước đã chốt."),
                formCard,
                actionRow(msg, calc),
                sectionTitle("Danh sách hóa đơn"),
                table,
                growSpacer()
        );
        box.setPadding(new Insets(4));
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node paymentPanel() {
        ComboBox<StaffDataStore.InvoiceItem> invoiceBox = new ComboBox<>();
        invoiceBox.setItems(controller.invoices().filtered(item -> !item.paid()));
        invoiceBox.setCellFactory(param -> invoiceCell());
        invoiceBox.setButtonCell(invoiceCell());

        ComboBox<String> method = new ComboBox<>();
        method.getItems().addAll("Tiền mặt", "Chuyển khoản");
        method.getSelectionModel().selectFirst();

        styleField(invoiceBox);
        styleField(method);

        Label msg = statusLabel();
        Button pay = new Button("Xác nhận đã thu");
        stylePrimaryButton(pay);

        pay.setOnAction(event -> runSafe(msg, () -> {
            StaffDataStore.InvoiceItem selected = invoiceBox.getValue();
            controller.markInvoicePaid(selected, method.getValue());
            invoiceBox.setItems(controller.invoices().filtered(item -> !item.paid()));
            msg.setText("Đã cập nhật trạng thái thanh toán");
        }));

        VBox formCard = fieldCard("Thông tin thanh toán",
                "Hóa đơn", invoiceBox,
                "Phương thức", method
        );

        TableView<StaffDataStore.InvoiceItem> table = invoiceTable(controller.invoices());
        VBox box = new VBox(16,
                sectionHeader("Xác nhận thanh toán", "Chọn hóa đơn chưa thu và cập nhật trạng thái thanh toán."),
                formCard,
                actionRow(msg, pay),
                sectionTitle("Theo dõi thanh toán"),
                table,
                growSpacer()
        );
        box.setPadding(new Insets(4));
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node invoiceLookupPanel() {
        TextField filter = new TextField();
        styleField(filter);
        filter.setPromptText("Nhập mã phòng để lọc hóa đơn...");

        FilteredList<StaffDataStore.InvoiceItem> filtered = new FilteredList<>(controller.invoices(), item -> true);
        filter.textProperty().addListener((obs, oldValue, value) -> {
            String key = value == null ? "" : value.trim().toLowerCase();
            filtered.setPredicate(item -> key.isEmpty() || item.roomCode().toLowerCase().contains(key));
        });

        VBox filterCard = fieldCard("Bộ lọc", "Mã phòng", filter);
        TableView<StaffDataStore.InvoiceItem> table = invoiceTable(filtered);

        VBox box = new VBox(16,
                sectionHeader("Tra cứu hóa đơn", "Tìm nhanh hóa đơn theo mã phòng và trạng thái thanh toán."),
                filterCard,
                sectionTitle("Kết quả tra cứu"),
                table,
                growSpacer()
        );
        box.setPadding(new Insets(4));
        VBox.setVgrow(table, Priority.ALWAYS);
        return box;
    }

    public Node feedbackPanel() {
        ComboBox<String> room = new ComboBox<>(controller.roomCodes());
        TextField title = new TextField();
        TextArea description = new TextArea();
        description.setPrefRowCount(3);
        ComboBox<String> priority = new ComboBox<>();
        priority.getItems().addAll("Thấp", "Trung bình", "Cao", "Khẩn cấp");
        priority.getSelectionModel().select(1);

        styleField(room);
        styleField(title);
        styleField(description);
        styleField(priority);
        room.setPromptText("Chọn phòng");
        title.setPromptText("Ví dụ: Hỏng đèn hành lang");
        description.setPromptText("Mô tả chi tiết sự cố...");

        Label msg = statusLabel();
        Button add = new Button("Ghi nhận");
        Button clear = new Button("Làm mới");
        stylePrimaryButton(add);
        styleSecondaryButton(clear);

        add.setOnAction(event -> runSafe(msg, () -> {
            controller.addFeedback(room.getValue(), title.getText(), description.getText(), priority.getValue());
            room.getSelectionModel().clearSelection();
            title.clear();
            description.clear();
            msg.setText("Đã ghi nhận sự cố");
        }));

        clear.setOnAction(event -> {
            room.getSelectionModel().clearSelection();
            title.clear();
            description.clear();
            msg.setText("Đã làm mới biểu mẫu");
            msg.setStyle("-fx-text-fill: #2563eb;");
        });

        VBox formCard = fieldCard("Gửi phản hồi / sự cố",
                "Phòng", room,
                "Tiêu đề", title,
                "Nội dung", description,
                "Mức độ", priority
        );

        TableView<StaffDataStore.FeedbackItem> table = new TableView<>(controller.feedbacks());
        table.getColumns().setAll(List.of(
                column("Mã", item -> String.valueOf(item.id()), 70),
                column("Phòng", StaffDataStore.FeedbackItem::roomCode, 90),
                column("Tiêu đề", StaffDataStore.FeedbackItem::title, 220),
                column("Mức độ", StaffDataStore.FeedbackItem::priority, 110),
                column("Trạng thái", StaffDataStore.FeedbackItem::status, 130)
        ));
        styleTable(table);

        VBox box = new VBox(16,
                sectionHeader("Ghi nhận sự cố / Phản hồi", "Tiếp nhận thông tin từ khách thuê để xử lý kịp thời."),
                formCard,
                actionRow(msg, add, clear),
                sectionTitle("Danh sách phản hồi"),
                table,
                growSpacer()
        );
        box.setPadding(new Insets(4));
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
        table.getColumns().setAll(List.of(
                column("Mã", item -> "HD-" + item.id(), 80),
                column("Phòng", StaffDataStore.InvoiceItem::roomCode, 90),
                column("Tháng", item -> item.month().toString(), 100),
                column("Tổng tiền", item -> formatMoney(item.totalAmount()), 130),
                column("Trạng thái", item -> item.paid() ? item.paymentMethod() : "Chưa thanh toán", 160)
        ));
        styleTable(table);
        return table;
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

    private static VBox form(Label message, Button action, Object... labelsAndFields) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < labelsAndFields.length; i += 2) {
            grid.addRow(i / 2, new Label((String) labelsAndFields[i]), (Node) labelsAndFields[i + 1]);
        }

        return new VBox(10, grid, new HBox(10, action, message));
    }

    private static VBox fieldCard(String heading, Object... labelsAndFields) {
        Label label = new Label(heading);
        label.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

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

        VBox box = new VBox(12, label, grid);
        box.setPadding(new Insets(16));
        box.setStyle(cardStyle("#ffffff", "#e2e8f0"));
        VBox.setVgrow(grid, Priority.ALWAYS);
        return box;
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

    private static <T> void styleTable(TableView<T> table) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setStyle(tableStyle());
    }

    private static String formatDate(LocalDate value) {
        return value == null ? "" : DATE_FORMAT.format(value);
    }

    private static String formatDateTime(java.time.LocalDateTime value) {
        return value == null ? "" : DATETIME_FORMAT.format(value);
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

    private static String nullableText(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static String shortPath(String path) {
        if (path == null || path.isBlank()) {
            return "";
        }
        int slash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return slash >= 0 ? path.substring(slash + 1) : path;
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

    private static java.time.LocalDate parseDate(String value, String field) {
        try {
            return java.time.LocalDate.parse(required(value, field));
        } catch (Exception ex) {
            throw new IllegalArgumentException(field + " phải theo định dạng YYYY-MM-DD");
        }
    }

    private static LocalDate requiredDate(LocalDate value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " không được để trống");
        }
        return value;
    }

    private static <T> T requireSelection(TableView<T> table, String message) {
        T selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            throw new IllegalArgumentException(message);
        }
        return selected;
    }

    private static String formatMoney(double value) {
        return String.format("%,.0f", value);
    }

    private String resolveBaseRentText(String roomCode) {
        if (homeCatalog == null) {
            return "";
        }
        java.math.BigDecimal baseRent = homeCatalog.findBaseRentByRoom(roomCode);
        if (baseRent == null) {
            return "";
        }
        return baseRent.stripTrailingZeros().toPlainString();
    }
}

