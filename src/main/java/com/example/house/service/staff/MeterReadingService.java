package com.example.house.service.staff;

import com.example.house.model.entity.UtilityReading;

import java.time.YearMonth;
import java.util.List;

public interface MeterReadingService {
    UtilityReading saveUtilityReading(String roomNumber, YearMonth period, int oldElectric, int newElectric,
                                      int oldWater, int newWater);
    List<UtilityReading> getUtilityReadings();
}


