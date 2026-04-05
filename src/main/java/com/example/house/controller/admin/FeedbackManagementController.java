package com.example.house.controller.admin;

import com.example.house.model.enums.FeedbackStatus;
import com.example.house.service.admin.AdminDataStore;
import com.example.house.service.admin.AdminService;
import javafx.collections.ObservableList;

public class FeedbackManagementController {
    private final AdminService service;

    public FeedbackManagementController(AdminService service) {
        this.service = service;
    }

    public ObservableList<AdminDataStore.FeedbackItem> feedbacks() {
        return service.feedbacks();
    }

    public void refreshAll() {
        service.refreshAll();
    }

    public void getFeedbacksByStatus(FeedbackStatus status) {
        service.getFeedbacksByStatus(status);
    }

    public AdminDataStore.FeedbackItem updateFeedbackStatus(Integer id, FeedbackStatus status) {
        return service.updateFeedbackStatus(id, status);
    }
}

