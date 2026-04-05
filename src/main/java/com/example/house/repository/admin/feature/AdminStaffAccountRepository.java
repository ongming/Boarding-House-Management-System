package com.example.house.repository.admin.feature;

import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public interface AdminStaffAccountRepository {
    ObservableList<AdminDataStore.StaffAccountItem> staffAccounts();
    AdminDataStore.StaffAccountItem createStaffAccount(String username, String password, String fullName, String shiftSchedule);
}

