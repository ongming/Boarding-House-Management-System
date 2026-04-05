package com.example.house.view.staff;

import com.example.house.controller.staff.home.StaffHomeCatalog;
import com.example.house.model.dto.staff.StaffRoomSnapshot;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.function.Consumer;

public class StaffHomePanel {
    private final BorderPane root = new BorderPane();
    private final StaffHomeCatalog catalog;
    private final Consumer<String> onCreateContract;
    private final ToggleGroup floorGroup = new ToggleGroup();
    private final FlowPane grid = new FlowPane(14, 14);
    private final VBox detailBox = new VBox(10);
    private final Label floorTitle = new Label();
    private final FilteredList<StaffRoomSnapshot> filteredRooms;
    private StaffRoomSnapshot selectedRoom;

    public StaffHomePanel(StaffHomeCatalog catalog, Consumer<String> onCreateContract) {
        this.catalog = catalog;
        this.onCreateContract = onCreateContract;
        this.filteredRooms = new FilteredList<>(catalog.getRooms(), room -> true);
        initialize();
    }

    public BorderPane getRoot() {
        return root;
    }

    private void initialize() {
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: #f5f7fa;");
        root.setTop(buildHeader());
        root.setCenter(buildContent());
        root.setRight(buildDetail());
        List<Integer> floors = catalog.getFloors();
        if (!floors.isEmpty()) {
            selectFloor(floors.get(0));
        }
    }

    private VBox buildHeader() {
        Label title = new Label("Trang chủ nhà trọ");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1f2937"));

        Label subtitle = new Label("Chọn tầng để xem sơ đồ phòng. Màu xanh là trống, vàng là đã thuê, xám là bảo trì.");
        subtitle.setFont(Font.font("Segoe UI", 13));
        subtitle.setTextFill(Color.web("#6b7280"));

        HBox floorRow = new HBox(10, buildLegend(), buildFloorButtons());
        floorRow.setAlignment(Pos.CENTER_LEFT);

        VBox header = new VBox(10, title, subtitle, floorRow);
        header.setPadding(new Insets(0, 0, 16, 0));
        return header;
    }

    private HBox buildLegend() {
        return new HBox(8,
                legendChip("Trống", "#dcfce7", "#166534"),
                legendChip("Đã thuê", "#fef3c7", "#92400e"),
                legendChip("Bảo trì", "#e5e7eb", "#374151")
        );
    }

    private Label legendChip(String text, String bg, String fg) {
        Label label = new Label(text);
        label.setStyle("-fx-background-color:" + bg + "; -fx-text-fill:" + fg
                + "; -fx-background-radius:999; -fx-padding:6 12; -fx-font-weight:bold;");
        label.setFont(Font.font("Segoe UI", 12));
        return label;
    }

    private HBox buildFloorButtons() {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        for (Integer floor : catalog.getFloors()) {
            ToggleButton btn = new ToggleButton("Tầng " + floor);
            btn.setToggleGroup(floorGroup);
            btn.setStyle(normalFloorStyle());
            btn.setOnAction(e -> selectFloor(floor));
            btn.selectedProperty().addListener((obs, oldV, selected) ->
                    btn.setStyle(selected ? selectedFloorStyle() : normalFloorStyle()));
            row.getChildren().add(btn);
        }
        return row;
    }

    private BorderPane buildContent() {
        grid.setPrefWrapLength(760);
        grid.setPadding(new Insets(8));

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color:transparent;");

        floorTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        floorTitle.setTextFill(Color.web("#1f2937"));

        VBox box = new VBox(12, floorTitle, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        box.setStyle("-fx-background-color:white; -fx-padding:16; -fx-background-radius:12;"
                + "-fx-effect:dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 3);");
        return new BorderPane(box);
    }

    private VBox buildDetail() {
        detailBox.setPrefWidth(320);
        detailBox.setPadding(new Insets(16));
        detailBox.setStyle("-fx-background-color:white; -fx-background-radius:12;"
                + "-fx-effect:dropshadow(three-pass-box, rgba(0,0,0,0.08), 12, 0, 0, 3);");
        showDetail(null);
        return detailBox;
    }

    private void selectFloor(int floor) {
        floorGroup.getToggles().forEach(toggle -> {
            if (toggle instanceof ToggleButton b) {
                b.setSelected(b.getText().equals("Tầng " + floor));
            }
        });

        filteredRooms.setPredicate(room -> room.floor() == floor);
        floorTitle.setText("Danh sách phòng tầng " + floor);
        List<StackPane> cards = filteredRooms.stream().map(this::buildCard).toList();
        grid.getChildren().setAll(cards);

        if (!filteredRooms.isEmpty()) {
            selectRoom(filteredRooms.get(0));
        } else {
            showDetail(null);
        }
    }

    private StackPane buildCard(StaffRoomSnapshot room) {
        Label number = new Label("Phòng " + room.roomNumber());
        number.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        number.setTextFill(Color.web("#111827"));

        Label status = new Label(room.displayStatus());
        status.setStyle(room.statusStyle() + " -fx-background-radius:999; -fx-padding:5 10;");
        status.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        Label summary = new Label(room.summaryText());
        summary.setTextFill(Color.web("#6b7280"));
        summary.setFont(Font.font("Segoe UI", 12));

        VBox card = new VBox(8, number, status, summary);
        card.setUserData(room);
        card.setPadding(new Insets(16));
        card.setPrefSize(230, 130);
        card.setMinSize(230, 130);
        card.setMaxSize(230, 130);
        card.setStyle(cardStyle(room, false));

        StackPane wrapper = new StackPane(card);
        wrapper.setOnMouseEntered(e -> card.setStyle(cardStyle(room, true)));
        wrapper.setOnMouseExited(e -> card.setStyle(cardStyle(room,
                selectedRoom != null && selectedRoom.roomNumber().equals(room.roomNumber()))));
        wrapper.setOnMouseClicked(e -> selectRoom(room));
        return wrapper;
    }

    private void selectRoom(StaffRoomSnapshot room) {
        selectedRoom = room;
        showDetail(room);

        grid.getChildren().forEach(node -> {
            if (node instanceof StackPane stack && !stack.getChildren().isEmpty() && stack.getChildren().get(0) instanceof VBox card
                    && !card.getChildren().isEmpty() && card.getChildren().get(0) instanceof Label title) {
                StaffRoomSnapshot cardRoom = (StaffRoomSnapshot) card.getUserData();
                boolean selected = title.getText().equals("Phòng " + room.roomNumber());
                card.setStyle(cardStyle(cardRoom == null ? room : cardRoom, selected));
            }
        });
    }

    private void showDetail(StaffRoomSnapshot room) {
        detailBox.getChildren().clear();
        Label title = new Label("Chi tiết phòng");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#1f2937"));
        detailBox.getChildren().add(title);

        if (room == null) {
            Label empty = new Label("Chọn một phòng để xem thông tin.");
            empty.setWrapText(true);
            empty.setTextFill(Color.web("#6b7280"));
            detailBox.getChildren().add(empty);
            return;
        }

        detailBox.getChildren().addAll(
                detailRow("Phòng", room.roomNumber()),
                detailRow("Tầng", String.valueOf(room.floor())),
                detailRow("Diện tích", String.format("%.0f m²", room.size())),
                detailRow("Giá thuê", formatMoney(room.baseRent().doubleValue())),
                detailRow("Trạng thái", room.displayStatus()),
                detailRow("Khách thuê", room.detailTenant()),
                detailRow("Số người ở", room.detailOccupantCount()),
                detailRow("Nội thất", room.furnitureList())
        );

        boolean canCreateContract = room.canCreateContract() && onCreateContract != null;
        Button contract = new Button(canCreateContract ? "Lập hợp đồng" : "Không thể lập hợp đồng");
        contract.setDisable(!canCreateContract);
        contract.setMaxWidth(Double.MAX_VALUE);
        contract.setStyle(canCreateContract
                ? "-fx-background-color: linear-gradient(to right, #22c55e, #16a34a);"
                + " -fx-text-fill:white; -fx-font-weight:bold; -fx-background-radius:8; -fx-padding:10;"
                : "-fx-background-color: #cbd5e1; -fx-text-fill:#475569;"
                + " -fx-font-weight:bold; -fx-background-radius:8; -fx-padding:10;");
        contract.setOnAction(e -> {
            if (canCreateContract) {
                onCreateContract.accept(room.roomNumber());
            }
        });
        detailBox.getChildren().add(contract);
    }

    private VBox detailRow(String label, String value) {
        Label left = new Label(label + ":");
        left.setStyle("-fx-text-fill:#6b7280; -fx-font-weight:bold;");
        Label right = new Label(value);
        right.setWrapText(true);
        right.setStyle("-fx-text-fill:#111827;");
        return new VBox(2, left, right);
    }

    private String cardStyle(StaffRoomSnapshot room, boolean selected) {
        String bg = switch (room.status()) {
            case TRONG -> "#f0fdf4";
            case DA_THUE -> "#fffbeb";
            case BAO_TRI -> "#f8fafc";
        };
        String border = selected ? "#1d4ed8" : switch (room.status()) {
            case TRONG -> "#86efac";
            case DA_THUE -> "#facc15";
            case BAO_TRI -> "#cbd5e1";
        };
        return "-fx-background-color:" + bg + "; -fx-border-color:" + border
                + "; -fx-border-width:1; -fx-border-radius:14; -fx-background-radius:14;"
                + " -fx-effect:dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 2);";
    }

    private String normalFloorStyle() {
        return "-fx-background-color:white; -fx-border-color:#d1d5db; -fx-border-radius:999;"
                + " -fx-background-radius:999; -fx-text-fill:#1f2937; -fx-font-weight:bold; -fx-padding:8 14;";
    }

    private String selectedFloorStyle() {
        return "-fx-background-color: linear-gradient(to right, #2563eb, #1d4ed8);"
                + " -fx-border-radius:999; -fx-background-radius:999; -fx-text-fill:white;"
                + " -fx-font-weight:bold; -fx-padding:8 14;";
    }

    private String formatMoney(double value) {
        return String.format("%,.0f", value);
    }
}
