package com.example.house.controller.staff;

import com.example.house.controller.shared.ControllerInputParser;
import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffService;
import javafx.collections.ObservableList;

public class OccupancyController {
    private final StaffService service;

    public OccupancyController(StaffService service) {
        this.service = service;
    }

    public ObservableList<StaffDataStore.OccupancyItem> occupancies() {
        return service.occupancies();
    }

    public void updateOccupancy(String roomCode, String peopleCount) {
        service.upsertOccupancy(
                ControllerInputParser.required(roomCode, "Phong"),
                ControllerInputParser.parseInt(peopleCount, "So nguoi")
        );
    }
}

