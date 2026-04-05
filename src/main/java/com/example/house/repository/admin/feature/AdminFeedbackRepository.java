package com.example.house.repository.admin.feature;

import com.example.house.model.enums.FeedbackStatus;
import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public interface AdminFeedbackRepository {
    ObservableList<AdminDataStore.FeedbackItem> feedbacks();
    AdminDataStore.FeedbackItem updateFeedbackStatus(Integer id, FeedbackStatus status);
    void getFeedbacksByStatus(FeedbackStatus status);
    void refreshAll();
}

