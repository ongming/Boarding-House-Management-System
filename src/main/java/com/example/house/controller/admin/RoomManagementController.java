package com.example.house.controller.admin;

import com.example.house.service.admin.AdminDataStore;
import com.example.house.service.admin.AdminService;
import javafx.collections.ObservableList;

public class RoomManagementController {
    private final AdminService service;

    public RoomManagementController(AdminService service) {
        this.service = service;
    }

    public ObservableList<AdminDataStore.RoomItem> rooms() {
        return service.rooms();
    }

    public AdminDataStore.RoomItem saveRoom(AdminDataStore.RoomItem item) {
        return service.saveRoom(item);
    }

    public void deleteRoom(Integer roomId) {
        service.deleteRoom(roomId);
    }
}
