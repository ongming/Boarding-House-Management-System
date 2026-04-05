package com.example.house.service.staff;

import com.example.house.model.entity.Feedback;
import com.example.house.model.enums.FeedbackStatus;

import java.util.List;

public interface IncidentService {
    Feedback saveIncident(Integer roomId, String content, Integer currentStaffId);
    Feedback updateIncidentStatus(Integer incidentId, FeedbackStatus status);
    List<Feedback> getFeedbacks();
}


