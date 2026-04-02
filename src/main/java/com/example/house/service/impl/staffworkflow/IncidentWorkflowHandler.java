package com.example.house.service.impl.staffworkflow;

import com.example.house.model.entity.Employee;
import com.example.house.model.entity.Feedback;
import com.example.house.model.entity.Room;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.repository.staff.EmployeeRepository;
import com.example.house.repository.staff.FeedbackRepository;
import com.example.house.repository.staff.RoomRepository;

import java.time.LocalDateTime;

public class IncidentWorkflowHandler {
    private final RoomRepository roomRepository;
    private final FeedbackRepository feedbackRepository;
    private final EmployeeRepository employeeRepository;

    public IncidentWorkflowHandler(RoomRepository roomRepository,
                                   FeedbackRepository feedbackRepository,
                                   EmployeeRepository employeeRepository) {
        this.roomRepository = roomRepository;
        this.feedbackRepository = feedbackRepository;
        this.employeeRepository = employeeRepository;
    }

    public Feedback saveIncident(Integer roomId, String content, Integer currentStaffId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Employee employee = employeeRepository.findById(currentStaffId)
                .or(employeeRepository::findFirst)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Feedback feedback = new Feedback();
        feedback.setRoom(room);
        feedback.setEmployee(employee);
        feedback.setContent(content);
        feedback.setStatus(FeedbackStatus.CHO_XU_LY);
        feedback.setSentAt(LocalDateTime.now());
        return feedbackRepository.save(feedback);
    }

    public Feedback updateIncidentStatus(Integer incidentId, FeedbackStatus status) {
        Feedback feedback = feedbackRepository.findById(incidentId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));
        feedback.setStatus(status);
        return feedbackRepository.save(feedback);
    }
}

