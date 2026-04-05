package com.example.house.model.dto.admin;

import com.example.house.model.enums.AdminRevenuePeriod;

import java.util.List;

public class AdminRevenueReport {
    private final AdminRevenuePeriod period;
    private final int year;
    private final List<AdminRevenueRow> rows;

    public AdminRevenueReport(AdminRevenuePeriod period, int year, List<AdminRevenueRow> rows) {
        this.period = period;
        this.year = year;
        this.rows = rows;
    }

    public AdminRevenuePeriod period() {
        return period;
    }

    public int year() {
        return year;
    }

    public List<AdminRevenueRow> rows() {
        return rows;
    }
}
