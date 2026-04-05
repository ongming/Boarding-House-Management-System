package com.example.house.service.staff;

import com.example.house.model.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle registerVehicle(String roomNumber, String vehicleType, String plateNumber);
    List<Vehicle> getVehicles();
}


