package com.example.house.service.admin;

import com.example.house.model.entity.Feedback;
import com.example.house.model.enums.FeedbackStatus;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getFeedbacks();
    List<Feedback> getFeedbacksByStatus(FeedbackStatus status);
    Feedback updateFeedbackStatus(Integer id, FeedbackStatus status);
}


