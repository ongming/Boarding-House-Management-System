package com.example.house.repository.impl.admin.feature;

import com.example.house.model.enums.FeedbackStatus;
import com.example.house.repository.admin.feature.AdminFeedbackRepository;
import com.example.house.service.admin.AdminDataStore;
import javafx.collections.ObservableList;

public class AdminFeedbackRepositoryImpl implements AdminFeedbackRepository {
    private final AdminDataStore dataStore;

    public AdminFeedbackRepositoryImpl(AdminDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ObservableList<AdminDataStore.FeedbackItem> feedbacks() {
        return dataStore.feedbacks();
    }

    @Override
    public AdminDataStore.FeedbackItem updateFeedbackStatus(Integer id, FeedbackStatus status) {
        return dataStore.updateFeedbackStatus(id, status);
    }

    @Override
    public void getFeedbacksByStatus(FeedbackStatus status) {
        dataStore.getFeedbacksByStatus(status);
    }

    @Override
    public void refreshAll() {
        dataStore.refreshAll();
    }
}
