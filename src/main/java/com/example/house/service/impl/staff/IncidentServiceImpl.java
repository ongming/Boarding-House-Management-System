package com.example.house.service.impl.staff;

import com.example.house.model.entity.Feedback;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.service.staff.IncidentService;
import com.example.house.service.staff.StaffDomainService;

import java.util.List;

public class IncidentServiceImpl implements IncidentService {
    private final StaffDomainService workflow;

    public IncidentServiceImpl() {
        this(new StaffDomainServiceImpl());
    }

    public IncidentServiceImpl(StaffDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public Feedback saveIncident(Integer roomId, String content, Integer currentStaffId) {
        return workflow.saveIncident(roomId, content, currentStaffId);
    }

    @Override
    public Feedback updateIncidentStatus(Integer incidentId, FeedbackStatus status) {
        return workflow.updateIncidentStatus(incidentId, status);
    }

    @Override
    public List<Feedback> getFeedbacks() {
        return workflow.getFeedbacks();
    }
}
