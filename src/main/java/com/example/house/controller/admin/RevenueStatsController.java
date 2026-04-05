package com.example.house.controller.admin;

import com.example.house.model.enums.AdminRevenuePeriod;
import com.example.house.service.admin.AdminDataStore;
import com.example.house.service.admin.AdminService;
import javafx.collections.ObservableList;

public class RevenueStatsController {
    private final AdminService service;

    public RevenueStatsController(AdminService service) {
        this.service = service;
    }

    public ObservableList<AdminDataStore.RevenueRow> revenueRows() {
        return service.revenueRows();
    }

    public void refreshRevenue(AdminRevenuePeriod period, int year, Integer periodValue) {
        service.refreshRevenue(period, year, periodValue);
    }
}

