package com.example.house.repository.impl.staff.feature;

import com.example.house.repository.staff.feature.StaffMeterReadingRepository;
import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

import java.time.YearMonth;

public class StaffMeterReadingRepositoryImpl implements StaffMeterReadingRepository {
    private final StaffDataStore dataStore;

    public StaffMeterReadingRepositoryImpl(StaffDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<StaffDataStore.MeterReadingItem> meterReadings() {
        return dataStore.meterReadings();
    }

    @Override
    public StaffDataStore.MeterReadingItem addMeterReading(String roomCode,
                                                           YearMonth month,
                                                           int oldElectric,
                                                           int newElectric,
                                                           int oldWater,
                                                           int newWater) {
        return dataStore.addMeterReading(roomCode, month, oldElectric, newElectric, oldWater, newWater);
    }
}
