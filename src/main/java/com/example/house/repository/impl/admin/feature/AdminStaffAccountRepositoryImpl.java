package com.example.house.repository.impl.admin.feature;

import com.example.house.repository.admin.feature.AdminStaffAccountRepository;
import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public class AdminStaffAccountRepositoryImpl implements AdminStaffAccountRepository {
    private final AdminDataStore dataStore;

    public AdminStaffAccountRepositoryImpl(AdminDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<AdminDataStore.StaffAccountItem> staffAccounts() {
        return dataStore.staffAccounts();
    }

    @Override
    public AdminDataStore.StaffAccountItem createStaffAccount(String username,
                                                              String password,
                                                              String fullName,
                                                              String shiftSchedule) {
        return dataStore.createStaffAccount(username, password, fullName, shiftSchedule);
    }
}
