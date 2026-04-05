package com.example.house.repository.staff;

import com.example.house.model.entity.RateConfig;
import com.example.house.model.enums.RateType;

import java.util.List;
import java.util.Optional;

public interface RateConfigRepository {
    List<RateConfig> findAll();

    Optional<RateConfig> findByType(RateType type);

    RateConfig save(RateConfig config);
}


