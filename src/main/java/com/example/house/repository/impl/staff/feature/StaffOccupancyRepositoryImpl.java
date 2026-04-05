package com.example.house.repository.impl.staff.feature;

import com.example.house.repository.staff.feature.StaffOccupancyRepository;
import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

public class StaffOccupancyRepositoryImpl implements StaffOccupancyRepository {
    private final StaffDataStore dataStore;

    public StaffOccupancyRepositoryImpl(StaffDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<StaffDataStore.OccupancyItem> occupancies() {
        return dataStore.occupancies();
    }

    @Override
    public StaffDataStore.OccupancyItem upsertOccupancy(String roomCode, int peopleCount) {
        return dataStore.upsertOccupancy(roomCode, peopleCount);
    }
}
