package com.example.house.repository.admin.feature;

import com.example.house.model.enums.RateType;
import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public interface AdminRateConfigRepository {
    ObservableList<AdminDataStore.RateItem> rateConfigs();
    AdminDataStore.RateItem saveRate(RateType type, double unitPrice);
}

