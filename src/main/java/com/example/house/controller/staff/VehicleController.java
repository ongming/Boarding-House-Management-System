package com.example.house.controller.staff;

import com.example.house.controller.shared.ControllerInputParser;
import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffService;
import javafx.collections.ObservableList;

public class VehicleController {
    private final StaffService service;

    public VehicleController(StaffService service) {
        this.service = service;
    }

    public ObservableList<StaffDataStore.VehicleItem> vehicles() {
        return service.vehicles();
    }

    public void addVehicle(String roomCode, String vehicleType, String plateNumber, String monthlyFee) {
        service.addVehicle(
                ControllerInputParser.required(roomCode, "Phong"),
                ControllerInputParser.required(vehicleType, "Loai xe"),
                ControllerInputParser.required(plateNumber, "Bien so"),
                ControllerInputParser.parseDouble(monthlyFee, "Phi xe")
        );
    }
}

