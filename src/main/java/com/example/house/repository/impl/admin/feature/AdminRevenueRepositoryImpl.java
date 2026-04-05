package com.example.house.repository.impl.admin.feature;

import com.example.house.model.enums.AdminRevenuePeriod;
import com.example.house.repository.admin.feature.AdminRevenueRepository;
import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public class AdminRevenueRepositoryImpl implements AdminRevenueRepository {
    private final AdminDataStore dataStore;

    public AdminRevenueRepositoryImpl(AdminDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<AdminDataStore.RevenueRow> revenueRows() {
        return dataStore.revenueRows();
    }

    @Override
    public void refreshRevenue(AdminRevenuePeriod period, int year, Integer periodValue) {
        dataStore.refreshRevenue(period, year, periodValue);
    }
}
