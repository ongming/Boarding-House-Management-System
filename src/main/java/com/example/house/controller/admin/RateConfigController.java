package com.example.house.controller.admin;

import com.example.house.model.enums.RateType;
import com.example.house.service.admin.AdminDataStore;
import com.example.house.service.admin.AdminService;
import javafx.collections.ObservableList;

public class RateConfigController {
    private final AdminService service;

    public RateConfigController(AdminService service) {
        this.service = service;
    }

    public ObservableList<AdminDataStore.RateItem> rateConfigs() {
        return service.rateConfigs();
    }

    public AdminDataStore.RateItem saveRate(RateType type, double unitPrice) {
        return service.saveRate(type, unitPrice);
    }
}

