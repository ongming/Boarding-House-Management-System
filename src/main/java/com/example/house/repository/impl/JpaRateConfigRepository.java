package com.example.house.repository.impl;

import com.example.house.model.entity.RateConfig;
import com.example.house.model.enums.RateType;
import com.example.house.repository.staff.RateConfigRepository;

import java.util.List;
import java.util.Optional;

public class JpaRateConfigRepository extends JpaRepositorySupport implements RateConfigRepository {
    @Override
    public List<RateConfig> findAll() {
        return withEntityManager(em -> em.createQuery(
                "SELECT r FROM RateConfig r", RateConfig.class)
                .getResultList());
    }

    @Override
    public Optional<RateConfig> findByType(RateType type) {
        return withEntityManager(em -> em.createQuery(
                "SELECT r FROM RateConfig r WHERE r.type = :type", RateConfig.class)
                .setParameter("type", type)
                .getResultStream()
                .findFirst());
    }
}

