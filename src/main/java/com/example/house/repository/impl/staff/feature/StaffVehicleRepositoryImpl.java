package com.example.house.repository.impl.staff.feature;

import com.example.house.repository.staff.feature.StaffVehicleRepository;
import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

public class StaffVehicleRepositoryImpl implements StaffVehicleRepository {
    private final StaffDataStore dataStore;

    public StaffVehicleRepositoryImpl(StaffDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<StaffDataStore.VehicleItem> vehicles() {
        return dataStore.vehicles();
    }

    @Override
    public StaffDataStore.VehicleItem addVehicle(String roomCode, String vehicleType, String plateNumber, double monthlyFee) {
        return dataStore.addVehicle(roomCode, vehicleType, plateNumber, monthlyFee);
    }
}
