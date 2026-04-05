package com.example.house.model.enums;

public enum AdminFeature {
    OVERVIEW("\ud83d\udcca", "Tổng quan"),
    RATE_CONFIG("\u2699", "Thiết lập đơn giá"),
    ROOM_MANAGEMENT("\ud83c\udfe2", "Quản lý tầng / phòng"),
    STAFF_ACCOUNT("\ud83d\udc64", "Cấp tài khoản nhân viên"),
    REVENUE_STATS("\ud83d\udcc8", "Thống kê doanh thu"),
    CHECKOUT_APPROVAL("\u2705", "Phê duyệt trả phòng"),
    FEEDBACK_MANAGEMENT("\ud83d\udee0", "Quản lý / chỉ đạo phản hồi"),
    INVOICE_LOOKUP("\ud83e\uddfe", "Tra cứu hóa đơn");

    private final String icon;
    private final String title;

    AdminFeature(String icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getMenuLabel() {
        return icon + " " + title;
    }
}
