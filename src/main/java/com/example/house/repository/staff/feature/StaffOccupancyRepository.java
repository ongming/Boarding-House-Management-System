package com.example.house.repository.staff.feature;

import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

public interface StaffOccupancyRepository {
    ObservableList<StaffDataStore.OccupancyItem> occupancies();
    StaffDataStore.OccupancyItem upsertOccupancy(String roomCode, int peopleCount);
}

