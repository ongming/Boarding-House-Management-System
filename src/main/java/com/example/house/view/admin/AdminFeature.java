package com.example.house.view.admin;

public enum AdminFeature {
    OVERVIEW("\ud83d\udcca", "Tong quan"),
    RATE_CONFIG("\u2699", "Thiet lap don gia"),
    ROOM_MANAGEMENT("\ud83c\udfe2", "Quan ly tang / phong"),
    STAFF_ACCOUNT("\ud83d\udc64", "Cap tai khoan nhan vien"),
    REVENUE_STATS("\ud83d\udcc8", "Thong ke doanh thu"),
    CHECKOUT_APPROVAL("\u2705", "Phe duyet tra phong"),
    FEEDBACK_MANAGEMENT("\ud83d\udee0", "Quan ly / chi dao phan hoi"),
    INVOICE_LOOKUP("\ud83e\uddfe", "Tra cuu hoa don");

    private final String icon;
    private final String title;

    AdminFeature(String icon, String title) {
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
