package com.example.house.model.dto.staff;

import com.example.house.model.enums.RoomStatus;

import java.math.BigDecimal;
import java.util.Locale;

public class StaffRoomSnapshot {
    private final String roomNumber;
    private final int floor;
    private final double size;
    private final BigDecimal baseRent;
    private final String furnitureList;
    private final RoomStatus status;
    private final String tenantName;
    private final Integer occupantCount;

    public StaffRoomSnapshot(String roomNumber,
                             int floor,
                             double size,
                             BigDecimal baseRent,
                             String furnitureList,
                             RoomStatus status,
                             String tenantName,
                             Integer occupantCount) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.size = size;
        this.baseRent = baseRent;
        this.furnitureList = furnitureList;
        this.status = status;
        this.tenantName = tenantName;
        this.occupantCount = occupantCount;
    }

    public String roomNumber() { return roomNumber; }
    public int floor() { return floor; }
    public double size() { return size; }
    public BigDecimal baseRent() { return baseRent; }
    public String furnitureList() { return furnitureList; }
    public RoomStatus status() { return status; }
    public String tenantName() { return tenantName; }
    public Integer occupantCount() { return occupantCount; }

    public String displayStatus() {
        return switch (status) {
            case TRONG -> "Trống";
            case DA_THUE -> "Đã có người thuê";
            case BAO_TRI -> "Bảo trì";
        };
    }

    public String statusStyle() {
        return switch (status) {
            case TRONG -> "-fx-background-color: #dcfce7; -fx-text-fill: #166534;";
            case DA_THUE -> "-fx-background-color: #fef3c7; -fx-text-fill: #92400e;";
            case BAO_TRI -> "-fx-background-color: #e5e7eb; -fx-text-fill: #374151;";
        };
    }

    public String summaryText() {
        return String.format(Locale.US, "%.0f m2 · %s", size, formatMoney(baseRent));
    }

    public boolean canCreateContract() {
        return status == RoomStatus.TRONG;
    }

    public String detailTenant() {
        return tenantName == null || tenantName.isBlank() ? "Chưa có người thuê" : tenantName;
    }

    public String detailOccupantCount() {
        return occupantCount == null ? "0" : String.valueOf(occupantCount);
    }

    private static String formatMoney(BigDecimal value) {
        return String.format("%,.0f", value == null ? 0 : value.doubleValue());
    }
}
