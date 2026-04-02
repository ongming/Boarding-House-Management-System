package com.example.house.repository.staff;

import com.example.house.model.entity.Contract;

import java.util.List;
import java.util.Optional;

public interface ContractRepository {
    Contract save(Contract contract);

    Optional<Contract> findById(Integer id);

    List<Contract> findAll();

    List<Contract> findActiveContracts();

    Optional<Contract> findActiveByRoomId(Integer roomId);
}

