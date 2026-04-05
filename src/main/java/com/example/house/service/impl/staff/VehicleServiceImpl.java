package com.example.house.service.impl.staff;

import com.example.house.model.entity.Vehicle;
import com.example.house.service.staff.StaffDomainService;
import com.example.house.service.staff.VehicleService;

import java.util.List;

public class VehicleServiceImpl implements VehicleService {
    private final StaffDomainService workflow;

    public VehicleServiceImpl() {
        this(new StaffDomainServiceImpl());
    }

    public VehicleServiceImpl(StaffDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public Vehicle registerVehicle(String roomNumber, String vehicleType, String plateNumber) {
        return workflow.registerVehicle(roomNumber, vehicleType, plateNumber);
    }

    @Override
    public List<Vehicle> getVehicles() {
        return workflow.getVehicles();
    }
}
