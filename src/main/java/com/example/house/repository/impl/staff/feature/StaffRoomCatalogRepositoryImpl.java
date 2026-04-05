package com.example.house.repository.impl.staff.feature;

import com.example.house.repository.staff.feature.StaffRoomCatalogRepository;
import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

public class StaffRoomCatalogRepositoryImpl implements StaffRoomCatalogRepository {
    private final StaffDataStore dataStore;

    public StaffRoomCatalogRepositoryImpl(StaffDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<String> roomCodes() {
        return dataStore.roomCodes();
    }
}
