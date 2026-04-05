package com.example.house.model.enums;

public enum StaffFeature {
    HOME("\ud83c\udfe0", "Trang chủ"),
    CONTRACT("\ud83d\udcc4", "Lập hợp đồng"),
    VEHICLE("\ud83d\ude97", "Quản lý xe"),
    OCCUPANCY("\ud83d\udc65", "Cập nhật số người ở"),
    METER_READING("\u26a1", "Chốt chỉ số điện nước"),
    BILLING("\ud83d\udcb0", "Tính toán / Xuất hóa đơn"),
    PAYMENT("\u2705", "Xác nhận thanh toán"),
    INVOICE_LOOKUP("\ud83d\udccb", "Xem hóa đơn"),
    FEEDBACK("\ud83d\udd27", "Ghi nhận sự cố / Phản hồi");

    private final String icon;
    private final String title;

    StaffFeature(String icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getMenuLabel() {
        return icon + " " + title;
    }
}
