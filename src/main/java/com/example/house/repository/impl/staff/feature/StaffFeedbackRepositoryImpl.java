package com.example.house.repository.impl.staff.feature;

import com.example.house.repository.staff.feature.StaffFeedbackRepository;
import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

public class StaffFeedbackRepositoryImpl implements StaffFeedbackRepository {
    private final StaffDataStore dataStore;

    public StaffFeedbackRepositoryImpl(StaffDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<StaffDataStore.FeedbackItem> feedbacks() {
        return dataStore.feedbacks();
    }

    @Override
    public StaffDataStore.FeedbackItem addFeedback(String roomCode, String title, String description, String priority) {
        return dataStore.addFeedback(roomCode, title, description, priority);
    }
}
