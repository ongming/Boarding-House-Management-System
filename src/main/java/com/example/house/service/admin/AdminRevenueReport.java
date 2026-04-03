package com.example.house.service.admin;

import java.util.List;

public record AdminRevenueReport(AdminRevenuePeriod period, int year, List<AdminRevenueRow> rows) {
}
