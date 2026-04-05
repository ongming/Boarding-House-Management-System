package com.example.house.controller.staff;

import com.example.house.controller.shared.ControllerInputParser;
import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffService;
import javafx.collections.ObservableList;

public class StaffFeedbackController {
    private final StaffService service;

    public StaffFeedbackController(StaffService service) {
        this.service = service;
    }

    public ObservableList<StaffDataStore.FeedbackItem> feedbacks() {
        return service.feedbacks();
    }

    public void addFeedback(String roomCode, String title, String description, String priority) {
        service.addFeedback(
                ControllerInputParser.required(roomCode, "Phong"),
                ControllerInputParser.required(title, "Tieu de"),
                ControllerInputParser.required(description, "Noi dung"),
                ControllerInputParser.required(priority, "Muc do")
        );
    }
}

