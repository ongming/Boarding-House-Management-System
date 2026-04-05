package com.example.house.controller.staff;

import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffService;
import javafx.collections.ObservableList;

import java.time.YearMonth;

public class MeterReadingController {
    private final StaffService service;

    public MeterReadingController(StaffService service) {
        this.service = service;
    }

    public ObservableList<StaffDataStore.MeterReadingItem> meterReadings() {
        return service.meterReadings();
    }

    public void saveMeterReading(String roomCode,
                                 String month,
                                 String oldElectric,
                                 String newElectric,
                                 String oldWater,
                                 String newWater) {
        service.addMeterReading(
                required(roomCode, "Phong"),
                parseMonth(month),
                parseInt(oldElectric, "Dien cu"),
                parseInt(newElectric, "Dien moi"),
                parseInt(oldWater, "Nuoc cu"),
                parseInt(newWater, "Nuoc moi")
        );
    }

    private static String required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " khong duoc de trong");
        }
        return value.trim();
    }

    private static int parseInt(String value, String field) {
        try {
            return Integer.parseInt(required(value, field));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(field + " phai la so nguyen");
        }
    }

    private static YearMonth parseMonth(String value) {
        try {
            return YearMonth.parse(required(value, "Thang"));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Thang phai theo dinh dang YYYY-MM");
        }
    }
}
