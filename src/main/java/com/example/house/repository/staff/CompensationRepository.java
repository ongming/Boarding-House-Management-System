package com.example.house.repository.staff;

import com.example.house.model.entity.Compensation;

import java.util.List;
import java.util.Optional;

public interface CompensationRepository {
    Compensation save(Compensation compensation);

    Optional<Compensation> findById(Integer id);

    List<Compensation> findByContractId(Integer contractId);

    List<Compensation> findByContractIdAndCollected(Integer contractId, boolean collected);
}

