package com.example.house.repository.staff.feature;

import com.example.house.service.staff.StaffDataStore;
import javafx.collections.ObservableList;

public interface StaffFeedbackRepository {
    ObservableList<StaffDataStore.FeedbackItem> feedbacks();
    StaffDataStore.FeedbackItem addFeedback(String roomCode, String title, String description, String priority);
}

