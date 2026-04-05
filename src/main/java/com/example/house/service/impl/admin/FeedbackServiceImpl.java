package com.example.house.service.impl.admin;

import com.example.house.model.entity.Feedback;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.service.admin.AdminDomainService;
import com.example.house.service.admin.FeedbackService;
import com.example.house.service.impl.admin.AdminDomainServiceImpl;

import java.util.List;

public class FeedbackServiceImpl implements FeedbackService {
    private final AdminDomainService workflow;

    public FeedbackServiceImpl() {
        this(new AdminDomainServiceImpl());
    }

    public FeedbackServiceImpl(AdminDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public List<Feedback> getFeedbacks() {
        return workflow.getFeedbacks();
    }

    @Override
    public List<Feedback> getFeedbacksByStatus(FeedbackStatus status) {
        return workflow.getFeedbacksByStatus(status);
    }

    @Override
    public Feedback updateFeedbackStatus(Integer id, FeedbackStatus status) {
        return workflow.updateFeedbackStatus(id, status);
    }
}

