package com.example.house.repository.staff;

import com.example.house.model.entity.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository {
    Feedback save(Feedback feedback);

    Optional<Feedback> findById(Integer id);

    List<Feedback> findAll();
}

