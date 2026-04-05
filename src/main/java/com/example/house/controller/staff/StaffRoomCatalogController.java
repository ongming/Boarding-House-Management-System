package com.example.house.controller.staff;

import com.example.house.service.staff.StaffService;
import javafx.collections.ObservableList;

public class StaffRoomCatalogController {
    private final StaffService service;

    public StaffRoomCatalogController(StaffService service) {
        this.service = service;
    }

    public ObservableList<String> roomCodes() {
        return service.roomCodes();
    }
}

