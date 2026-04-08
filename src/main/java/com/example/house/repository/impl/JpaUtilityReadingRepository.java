package com.example.house.repository.impl;

import com.example.house.model.entity.UtilityReading;
import com.example.house.repository.UtilityReadingRepository;

import java.util.List;
import java.util.Optional;

public class JpaUtilityReadingRepository extends JpaRepositorySupport implements UtilityReadingRepository {
    @Override
    public UtilityReading save(UtilityReading reading) {
        return inTransaction(em -> {
            if (reading.getId() == null) {
                em.persist(reading);
                return reading;
            }
            return em.merge(reading);
        });
    }

    @Override
    public List<UtilityReading> findAll() {
        return withEntityManager(em -> em.createQuery(
                        "SELECT u FROM UtilityReading u ORDER BY u.id DESC", UtilityReading.class)
                .getResultList());
    }

    @Override
    public Optional<UtilityReading> findByRoomAndPeriod(Integer roomId, int month, int year) {
        return withEntityManager(em -> em.createQuery(
                        "SELECT u FROM UtilityReading u WHERE u.room.id = :roomId AND u.month = :month AND u.year = :year",
                        UtilityReading.class)
                .setParameter("roomId", roomId)
                .setParameter("month", month)
                .setParameter("year", year)
                .getResultStream()
                .findFirst());
    }

    @Override
    public List<UtilityReading> findByPeriod(int month, int year) {
        return withEntityManager(em -> em.createQuery(
                        "SELECT u FROM UtilityReading u WHERE u.month = :month AND u.year = :year", UtilityReading.class)
                .setParameter("month", month)
                .setParameter("year", year)
                .getResultList());
    }
}


