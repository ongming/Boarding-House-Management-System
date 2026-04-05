package com.example.house.service.staff;

import com.example.house.model.entity.Contract;
import com.example.house.model.entity.Room;

import java.util.List;

public interface StaffDomainService extends ContractService,
        VehicleService,
        OccupancyService,
        MeterReadingService,
        BillingService,
        PaymentService,
        IncidentService {
    List<Room> getRooms();
    List<Contract> getContracts();
}

