package com.example.house.repository.staff.feature;

import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

import java.time.YearMonth;

public interface StaffMeterReadingRepository {
    ObservableList<StaffDataStore.MeterReadingItem> meterReadings();
    StaffDataStore.MeterReadingItem addMeterReading(String roomCode,
                                                    YearMonth month,
                                                    int oldElectric,
                                                    int newElectric,
                                                    int oldWater,
                                                    int newWater);
}

