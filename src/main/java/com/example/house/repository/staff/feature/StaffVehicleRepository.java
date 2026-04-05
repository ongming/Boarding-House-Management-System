package com.example.house.repository.staff.feature;

import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

public interface StaffVehicleRepository {
    ObservableList<StaffDataStore.VehicleItem> vehicles();
    StaffDataStore.VehicleItem addVehicle(String roomCode, String vehicleType, String plateNumber, double monthlyFee);
}

