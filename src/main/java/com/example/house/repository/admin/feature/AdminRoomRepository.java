package com.example.house.repository.admin.feature;

import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public interface AdminRoomRepository {
    ObservableList<AdminDataStore.RoomItem> rooms();
    AdminDataStore.RoomItem saveRoom(AdminDataStore.RoomItem item);
    void deleteRoom(Integer roomId);
}

