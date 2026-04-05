package com.example.house.service.impl.staff;

import com.example.house.model.entity.UtilityReading;
import com.example.house.service.staff.MeterReadingService;
import com.example.house.service.staff.StaffDomainService;

import java.time.YearMonth;
import java.util.List;

public class MeterReadingServiceImpl implements MeterReadingService {
    private final StaffDomainService workflow;

    public MeterReadingServiceImpl() {
        this(new StaffDomainServiceImpl());
    }

    public MeterReadingServiceImpl(StaffDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public UtilityReading saveUtilityReading(String roomNumber, YearMonth period, int oldElectric, int newElectric,
                                             int oldWater, int newWater) {
        return workflow.saveUtilityReading(roomNumber, period, oldElectric, newElectric, oldWater, newWater);
    }

    @Override
    public List<UtilityReading> getUtilityReadings() {
        return workflow.getUtilityReadings();
    }
}
