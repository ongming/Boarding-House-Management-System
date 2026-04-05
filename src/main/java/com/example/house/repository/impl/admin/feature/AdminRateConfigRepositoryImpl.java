package com.example.house.repository.impl.admin.feature;

import com.example.house.model.enums.RateType;
import com.example.house.repository.admin.feature.AdminRateConfigRepository;
import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public class AdminRateConfigRepositoryImpl implements AdminRateConfigRepository {
    private final AdminDataStore dataStore;

    public AdminRateConfigRepositoryImpl(AdminDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<AdminDataStore.RateItem> rateConfigs() {
        return dataStore.rateConfigs();
    }

    @Override
    public AdminDataStore.RateItem saveRate(RateType type, double unitPrice) {
        return dataStore.saveRate(type, unitPrice);
    }
}
