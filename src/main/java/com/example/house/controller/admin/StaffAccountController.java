package com.example.house.controller.admin;

import com.example.house.service.admin.AdminDataStore;
import com.example.house.service.admin.AdminService;
import javafx.collections.ObservableList;

public class StaffAccountController {
    private final AdminService service;

    public StaffAccountController(AdminService service) {
        this.service = service;
    }

    public ObservableList<AdminDataStore.StaffAccountItem> staffAccounts() {
        return service.staffAccounts();
    }

    public AdminDataStore.StaffAccountItem createStaffAccount(String username,
                                                              String password,
                                                              String fullName,
                                                              String shiftSchedule) {
        return service.createStaffAccount(username, password, fullName, shiftSchedule);
    }
}

