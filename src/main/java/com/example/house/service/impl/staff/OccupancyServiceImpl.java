package com.example.house.service.impl.staff;

import com.example.house.model.entity.Contract;
import com.example.house.service.staff.OccupancyService;
import com.example.house.service.staff.StaffDomainService;

public class OccupancyServiceImpl implements OccupancyService {
    private final StaffDomainService workflow;

    public OccupancyServiceImpl() {
        this(new StaffDomainServiceImpl());
    }

    public OccupancyServiceImpl(StaffDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public Contract updateOccupancy(String roomNumber, int peopleCount) {
        return workflow.updateOccupancy(roomNumber, peopleCount);
    }
}
