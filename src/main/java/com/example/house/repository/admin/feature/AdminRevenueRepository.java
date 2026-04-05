package com.example.house.repository.admin.feature;

import com.example.house.model.enums.AdminRevenuePeriod;
import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public interface AdminRevenueRepository {
    ObservableList<AdminDataStore.RevenueRow> revenueRows();
    void refreshRevenue(AdminRevenuePeriod period, int year, Integer periodValue);
}

