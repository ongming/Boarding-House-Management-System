package com.example.house.repository.impl.admin.feature;

import com.example.house.repository.admin.feature.AdminRoomRepository;
import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public class AdminRoomRepositoryImpl implements AdminRoomRepository {
    private final AdminDataStore dataStore;

    public AdminRoomRepositoryImpl(AdminDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<AdminDataStore.RoomItem> rooms() {
        return dataStore.rooms();
    }

    @Override
    public AdminDataStore.RoomItem saveRoom(AdminDataStore.RoomItem item) {
        return dataStore.saveRoom(item);
    }

    @Override
    public void deleteRoom(Integer roomId) {
        dataStore.deleteRoom(roomId);
    }
}
