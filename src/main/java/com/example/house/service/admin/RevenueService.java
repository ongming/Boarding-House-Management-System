package com.example.house.service.admin;

import com.example.house.model.dto.admin.AdminRevenueReport;
import com.example.house.model.enums.AdminRevenuePeriod;

public interface RevenueService {
    AdminRevenueReport getRevenueReport(AdminRevenuePeriod period, int year);
}
