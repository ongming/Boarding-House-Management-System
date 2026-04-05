package com.example.house.repository.impl;

import com.example.house.model.entity.Feedback;
import com.example.house.model.enums.FeedbackStatus;
import com.example.house.repository.staff.FeedbackRepository;

import java.util.List;
import java.util.Optional;

public class JpaFeedbackRepository extends JpaRepositorySupport implements FeedbackRepository {
    @Override
    public Feedback save(Feedback feedback) {
        return inTransaction(em -> {
            if (feedback.getId() == null) {
                em.persist(feedback);
                return feedback;
            }
            return em.merge(feedback);
        });
    }

    @Override
    public Optional<Feedback> findById(Integer id) {
        return withEntityManager(em -> Optional.ofNullable(em.find(Feedback.class, id)));
    }

    @Override
    public List<Feedback> findAll() {
        return withEntityManager(em -> em.createQuery(
                "SELECT f FROM Feedback f ORDER BY f.id DESC", Feedback.class)
                .getResultList());
    }

    @Override
    public List<Feedback> findByStatus(FeedbackStatus status) {
        return withEntityManager(em -> em.createQuery(
                "SELECT f FROM Feedback f WHERE f.status = :status ORDER BY f.id DESC", Feedback.class)
                .setParameter("status", status)
                .getResultList());
    }
}


