package com.example.house.repository;

import com.example.house.model.entity.UtilityReading;

import java.util.List;
import java.util.Optional;

public interface UtilityReadingRepository {
    UtilityReading save(UtilityReading reading);

    List<UtilityReading> findAll();

    Optional<UtilityReading> findByRoomAndPeriod(Integer roomId, int month, int year);

    List<UtilityReading> findByPeriod(int month, int year);
}
