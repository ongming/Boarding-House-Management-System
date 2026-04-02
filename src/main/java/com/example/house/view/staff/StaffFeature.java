package com.example.house.view.staff;

public enum StaffFeature {
    HOME("🏠", "Trang chủ"),
    CONTRACT("📄", "Lập hợp đồng"),
    VEHICLE("🚗", "Quản lý xe"),
    OCCUPANCY("👥", "Cập nhật số người ở"),
    METER_READING("⚡", "Chốt chỉ số điện nước"),
    BILLING("💰", "Tính toán / Xuất hóa đơn"),
    PAYMENT("✅", "Xác nhận thanh toán"),
    INVOICE_LOOKUP("📋", "Xem hóa đơn"),
    FEEDBACK("🔧", "Ghi nhận sự cố / Phản hồi");

    private final String icon;
    private final String title;

    StaffFeature(String icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getMenuLabel() {
        return icon + " " + title;
    }
}
